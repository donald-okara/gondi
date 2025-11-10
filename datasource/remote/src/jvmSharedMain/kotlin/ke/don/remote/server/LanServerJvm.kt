/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.ModeratorEngine
import ke.don.domain.gameplay.server.ClientUpdate
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.Player
import ke.don.local.db.LocalDatabase
import ke.don.remote.gameplay.validateIntent
import ke.don.remote.gameplay.validationMessage
import ke.don.utils.Logger
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class LanServerJvm(
    private val advertiser: LanAdvertiser,
    private val database: LocalDatabase,
    private val gameEngine: GameEngine,
    private val moderatorEngine: ModeratorEngine,
) : LocalServer {

    private val logger = Logger("LanServer_JVM")
    private var server: EmbeddedServer<*, *>? = null

    // 👇 Keep track of all connected WebSocket sessions

    // 👇 Provide a Json instance for encoding messages
    private val json = Json { prettyPrint = true }

    private val sessions = ConcurrentHashMap.newKeySet<DefaultWebSocketServerSession>()

    override suspend fun start(identity: GameIdentity) {
        val host = getLocalIpAddress()

        server = embeddedServer(CIO, port = identity.servicePort, host = host) {
            install(ContentNegotiation) { json() }

            install(WebSockets) {
                pingPeriod = 15.seconds
                timeout = 30.seconds
                maxFrameSize = 10 * 1024 * 1024 // 10MB
                masking = false
            }

            routing {
                webSocket("/game") {
                    // Register this new client session
                    sessions += this
                    send(json.encodeToString(ServerUpdate.serializer(), ServerUpdate.Announcement("Connected to Gondi server ✅")))

                    try {
                        incoming.consumeEach { frame ->
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                handleClientMessage(identity.id, text)
                            }
                        }
                    } finally {
                        // Remove session on disconnect
                        sessions -= this
                    }
                }
            }
        }.start(wait = false)

        advertiser.start(
            gameIdentity = identity.copy(serviceHost = host),
        )

        logger.debug("✅ LAN WebSocket server started and advertised on ws://$host:${identity.servicePort}/game")
    }

    override suspend fun stop() {
        advertiser.stop()
        server?.stop()
    }

    override suspend fun handleModeratorCommand(gameId: String, command: ModeratorCommand) {
        moderatorEngine.handle(gameId, command)

        // After moderator actions, broadcast updates
        val newState = database.getGameState(gameId).firstOrNull()
        val players = database.getAllPlayersSnapshot()
        broadcast(ServerUpdate.GameStateSnapshot(newState))
        broadcast(ServerUpdate.PlayersSnapshot(players))

        logger.debug("Moderator command executed ✅ ($command)")
    }

    suspend fun DefaultWebSocketServerSession.sendJson(message: ClientUpdate) {
        send(json.encodeToString(message))
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun DefaultWebSocketServerSession.handleClientMessage(
        gameId: String,
        json: String,
    ) {
        try {
            val message = this@LanServerJvm.json.decodeFromString<ClientUpdate>(json)
            when (message) {
                is ClientUpdate.PlayerIntentMsg -> {
                    if (!validateIntent(gameId = gameId, db = database, intent = message.intent)) {
                        send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.Error(message.intent.validationMessage)))
                        return
                    }

                    // 1️⃣ Apply the intent
                    gameEngine.reduce(gameId, message.intent)

                    // 2️⃣ Broadcast updated state
                    val newState = database.getGameState(id = gameId).firstOrNull()
                    val players = database.getAllPlayersSnapshot()
                    broadcast(ServerUpdate.GameStateSnapshot(newState))
                    broadcast(ServerUpdate.PlayersSnapshot(players))

                    send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.Announcement("Intent processed ✅")))
                }
                is ClientUpdate.GetGameState -> {
                    val newState = database.getGameState(id = gameId).firstOrNull()
                    val players = database.getAllPlayersSnapshot()

                    send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.GameStateSnapshot(newState)))
                    send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.PlayersSnapshot(players)))
                }
                is ClientUpdate.Ping -> {
                    send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.LastPing(Clock.System.now().toEpochMilliseconds())))
                }
            }
        } catch (e: Exception) {
            send(Json.encodeToString(ServerUpdate.serializer(), ServerUpdate.Error("Error: ${e.message}")))
        }
    }

    private suspend fun broadcast(update: ServerUpdate) {
        val text = Json.encodeToString(update)
        sessions.forEach { it.send(Frame.Text(text)) }
    }
}

suspend fun LocalDatabase.getAllPlayersSnapshot(): List<Player> =
    getAllPlayers().firstOrNull() ?: emptyList()
