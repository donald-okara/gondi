/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.useCases

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientUpdate
import ke.don.domain.gameplay.server.ServerId
import ke.don.koffee.model.ToastDuration
import ke.don.remote.server.ClientObject
import ke.don.utils.Logger
import ke.don.utils.result.LocalError
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.concurrent.Volatile
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class GameClientManager(
    private val clientState: GameClientState,
) {
    private var session: DefaultClientWebSocketSession? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var pingJob: Job? = null

    @Volatile
    private var lastPingMillis: Instant = Clock.System.now()

    val logger = Logger("ClientManager")

    suspend fun connect(serverId: ServerId): Result<Unit, LocalError> {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Reconnecting"))

        clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Loading) }

        return connectWithRetry(serverId.first, serverId.second)
    }

    private suspend fun connectWithRetry(
        host: String,
        port: Int,
        maxRetries: Int = 5,
    ): Result<Unit, LocalError> {
        var attempt = 0

        while (attempt < maxRetries) {
            try {
                connectOnce(host, port)
                clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Success) }

                return Result.Success(Unit)
            } catch (e: Exception) {
                attempt++

                if (attempt >= maxRetries) {
                    clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Error(e.message ?: "Unknown")) }
                    logger.error("Connection failed after $maxRetries attempts: ${e.message}")

                    return Result.Error(
                        LocalError(
                            message = e.message.toString(),
                            cause = e.cause.toString(),
                        ),
                    )
                }

                clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Loading) }

                Matcha.info(
                    title = "Connection failed. Attempting retry...",
                    description = "Retrying...",
                )

                delay((attempt * 2000L).coerceAtMost(10_000L))
            }
        }

        return Result.Error(
            LocalError(
                message = "Connection failed after $maxRetries attempts",
                cause = "Unknown",
            ),
        )
    }

    private suspend fun connectOnce(host: String, port: Int) {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Reconnecting"))
        try {
            val player = clientState.profileSnapshot.first()?.toPlayer()
                ?: error("Player must be loaded before connecting")

            ClientObject.client.webSocket("ws://$host:$port/game") {
                session = this
                logger.info("Connected ✅")

                startPing()
                send(
                    Frame.Text(
                        Json.encodeToString(
                            ClientUpdate.serializer(),
                            ClientUpdate.GetGameState,
                        ),
                    ),
                )
                logger.info(
                    "Player: $player",
                )
                val joinMessage = ClientUpdate.PlayerIntentMsg(
                    PlayerIntent.Join(
                        playerId = player.id,
                        round = 0,
                        player = player,
                    ),
                )

                send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), joinMessage)))

                player.captureEvent(
                    "Joined game"
                )

                for (frame in incoming) {
                    lastPingMillis = Clock.System.now()
                    if (frame is Frame.Text) clientState.handleServerUpdate(frame.readText())
                }
            }
        } catch (e: Exception) {
            logger.error("Connection error: ${e.message}")
            clientState.updatePlayerState {
                it.copy(
                    connectionStatus = ReadStatus.Error(
                        e.message ?: "Unknown",
                    ),
                )
            }
        }
    }

    suspend fun send(message: ClientUpdate) {
        session?.send(
            Frame.Text(
                Json.encodeToString(
                    ClientUpdate.serializer(),
                    message,
                ),
            ),
        )
            ?: logger.error("⚠️ Not connected to server")
    }

    private fun startPing() {
        pingJob = scope.launch {
            while (isActive && session != null) {
                delay(10_000)
                try {
                    session?.send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), ClientUpdate.Ping)))
                } catch (e: Exception) {
                    val elapsed = Clock.System.now().minus(lastPingMillis)
                    logger.error("Ping failed: ${e.message}")

                    if (elapsed > 1.minutes) {
                        Matcha.error("Connection lost", duration = ToastDuration.Indefinite)
                        clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Error("Connection lost")) }
                        break
                    } else if (elapsed > 30.seconds) {
                        Matcha.warning("Connection failed. Retrying...")
                    }
                }
            }
        }
    }

    suspend fun dispose() {
        logger.info(
            "Disposing client manager",
        )

        val player = clientState.currentPlayer.first()
            ?: return

        val leaveMessage = ClientUpdate.PlayerIntentMsg(
            PlayerIntent.Leave(
                playerId = player.id,
                round = 0,
            ),
        )

        // Send leave safely
        session?.let { s ->
            try {
                withContext(NonCancellable) {
                    logger.debug("Attempting to leave")
                    s.send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), leaveMessage)))
                }
            } catch (e: Exception) {
                logger.error("Failed to send leave message: ${e.message}")
            }
        } ?: logger.debug("Session is null")

        pingJob?.cancelAndJoin()

        pingJob = null

        // Close session after sending
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Disposed"))
        session = null

        // Cancel all remaining jobs in this scope
        // Note: If reconnection is needed, recreate the scope or use job-based cancellation
        scope.coroutineContext[Job]?.cancelChildren()
    }
}
