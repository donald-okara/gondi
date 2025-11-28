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
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientUpdate
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.PhaseValidationResult
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.local.db.LocalDatabase
import ke.don.remote.gameplay.validateIntent
import ke.don.utils.Logger
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val _localEvents = MutableSharedFlow<ServerUpdate.Announcement>()
    override val localEvents: SharedFlow<ServerUpdate.Announcement> = _localEvents

    private val logger = Logger("LanServer_JVM")
    private var server: EmbeddedServer<*, *>? = null

    // 👇 Keep track of all connected WebSocket sessions

    // 👇 Provide a Json instance for encoding messages
    private val json = Json { prettyPrint = true }

    private val sessions = ConcurrentHashMap.newKeySet<DefaultWebSocketServerSession>()

    override suspend fun start(identity: GameIdentity) {
        advertiser.stop()
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
                    send(json.encodeToString(ServerUpdate.serializer(), ServerUpdate.Announcement("Connected to ${identity.gameName}")))

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
        database.clearVotes()
        database.clearGameState()
        database.clearPlayers()
        logger.debug("✅ LAN WebSocket server stopped")
    }
    private suspend fun announce(message: String) {
        broadcast(ServerUpdate.Announcement(message))
        _localEvents.emit(ServerUpdate.Announcement(message))
    }

    override suspend fun handleModeratorCommand(gameId: String, command: ModeratorCommand) {
        moderatorEngine.handle(gameId, command).also {
            when (command) {
                is ModeratorCommand.RemovePlayer -> {
                    val affectedPlayerId = command.playerId

                    val name = database.getAllPlayersSnapshot()
                        .firstOrNull { it.id == affectedPlayerId }
                        ?.name
                    name?.let{
                        val message = "$it has been kicked out of the game."
                        announce(message)
                    }
                }
                else -> {}
            }
        }

        // After moderator actions, broadcast updates
        val newState = database.getGameState(gameId).firstOrNull()
        val players = database.getAllPlayersSnapshot()
        val votes = database.getAllVotesSnapshot()
        broadcast(ServerUpdate.GameStateSnapshot(newState))
        broadcast(ServerUpdate.PlayersSnapshot(players))
        broadcast(ServerUpdate.VotesSnapshot(votes))
    }

    suspend fun DefaultWebSocketServerSession.sendJson(message: ClientUpdate) {
        send(json.encodeToString(message))
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun DefaultWebSocketServerSession.handleClientMessage(
        gameId: String,
        json: String,
    ) {
        fun encode(update: ServerUpdate) =
            Json.encodeToString(ServerUpdate.serializer(), update)

        suspend fun broadcastSnapshots(gameId: String) {
            val state = database.getGameState(gameId).firstOrNull()
            val players = database.getAllPlayersSnapshot()
            val votes = database.getAllVotesSnapshot()

            broadcast(ServerUpdate.GameStateSnapshot(state))
            broadcast(ServerUpdate.PlayersSnapshot(players))
            broadcast(ServerUpdate.VotesSnapshot(votes))
        }

        try {
            val message = this@LanServerJvm.json.decodeFromString<ClientUpdate>(json)

            when (message) {
                is ClientUpdate.PlayerIntentMsg -> {
                    when (val result = validateIntent(database, gameId, message.intent)) {
                        is PhaseValidationResult.Success -> {
                            gameEngine.reduce(gameId, message.intent)

                            // announcements only for join/leave
                            val affectedPlayerId = when (val intent = message.intent) {
                                is PlayerIntent.Leave -> intent.playerId
                                is PlayerIntent.Join -> intent.playerId
                                else -> null
                            }

                            if (affectedPlayerId != null) {
                                val name = database.getAllPlayersSnapshot()
                                    .firstOrNull { it.id == affectedPlayerId }
                                    ?.name

                                val verb = when (message.intent) {
                                    is PlayerIntent.Leave -> "left"
                                    is PlayerIntent.Join -> "joined"
                                    else -> null
                                }

                                if (name != null && verb != null) {
                                    val message = "$name has $verb the game."
                                    announce(message)
                                }
                            }

                            broadcastSnapshots(gameId)
                        }

                        is PhaseValidationResult.Error -> {
                            send(encode(ServerUpdate.Forbidden(result.message)))
                        }
                    }
                }

                is ClientUpdate.GetGameState -> {
                    val state = database.getGameState(gameId).firstOrNull()
                    val players = database.getAllPlayersSnapshot()
                    val votes = database.getAllVotesSnapshot()

                    send(encode(ServerUpdate.GameStateSnapshot(state)))
                    send(encode(ServerUpdate.PlayersSnapshot(players)))
                    send(encode(ServerUpdate.VotesSnapshot(votes)))
                }

                is ClientUpdate.Ping -> {
                    val now = Clock.System.now().toEpochMilliseconds()
                    send(encode(ServerUpdate.LastPing(now)))
                }
            }
        } catch (e: Exception) {
            send(encode(ServerUpdate.Error("Error: ${e.message}")))
        }
    }

    private suspend fun broadcast(update: ServerUpdate) {
        val text = Json.encodeToString(update)
        sessions.forEach { it.send(Frame.Text(text)) }
    }
}

suspend fun LocalDatabase.getAllPlayersSnapshot(): List<Player> =
    getAllPlayers().firstOrNull() ?: emptyList()

suspend fun LocalDatabase.getAllVotesSnapshot(): List<Vote> =
    getAllVotes().firstOrNull() ?: emptyList()
