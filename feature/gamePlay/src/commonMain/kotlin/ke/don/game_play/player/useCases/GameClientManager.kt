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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.concurrent.Volatile
import kotlin.time.Clock
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
    private var watchdogJob: Job? = null


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
        maxRetries: Int = 5
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
                            cause = e.cause.toString()
                        )
                    )
                }

                clientState.updatePlayerState { it.copy(connectionStatus = ReadStatus.Loading) }

                Matcha.info(
                    title = "Connection failed. Attempting retry...",
                    description = "Retrying..."
                )

                delay((attempt * 2000L).coerceAtMost(10_000L))
            }
        }

        return Result.Error(
            LocalError(
                message = "Connection failed after $maxRetries attempts",
                cause = "Unknown"
            )
        )
    }


    private fun connectOnce(host: String, port: Int) {
        scope.launch{
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "Reconnecting"))
            try {
                val player = clientState.currentPlayer.first()
                    ?: error("Player must be loaded before connecting")

                ClientObject.client.webSocket("ws://$host:$port/game") {
                    session = this
                    logger.info("Connected ✅")

                    startPing()
                    startWatchdog()
                    send(
                        Frame.Text(
                            Json.encodeToString(
                                ClientUpdate.serializer(),
                                ClientUpdate.GetGameState
                            )
                        )
                    )
                    logger.info(
                        "Player: $player"
                    )
                    val joinMessage = ClientUpdate.PlayerIntentMsg(
                        PlayerIntent.Join(
                            playerId = player.id,
                            round = 0,
                            player = player
                        )
                    )

                    send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), joinMessage)))

                    for (frame in incoming) {
                        lastPingMillis = Clock.System.now()
                        if (frame is Frame.Text) clientState.handleServerUpdate(frame.readText())
                    }
                }
            } catch (e: Exception) {
                logger.error("Connection error: ${e.message}")
                throw e // bubble up to retry
            }
        }
    }

    suspend fun send(message: ClientUpdate) {
        session?.send(
            Frame.Text(
                Json.encodeToString(
                    ClientUpdate.serializer(),
                    message
                )
            )
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
                    logger.error("Ping failed: ${e.message}")
                }
            }

        }
    }

    @OptIn(ExperimentalTime::class)
    private fun startWatchdog() {
        watchdogJob = scope.launch {
            while (isActive) {
                delay(15_000)

                val elapsed = Clock.System.now().minus(lastPingMillis)
                if (elapsed > 20.seconds) {
                    logger.error("⚠️ Server unresponsive. Reconnecting...")
                    session?.close(CloseReason(CloseReason.Codes.GOING_AWAY, "Ping timeout"))
                    return@launch
                }
            }
        }
    }

    suspend fun dispose() {
        logger.info(
            "Disposing client manager"
        )

       val player = clientState.currentPlayer.first()
            ?: error("Player must be loaded before connecting")

        val leaveMessage = ClientUpdate.PlayerIntentMsg(
            PlayerIntent.Leave(
                playerId = player.id,
                round = 0,
            )
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
        watchdogJob?.cancelAndJoin()

        pingJob = null
        watchdogJob = null

        // Close session after sending
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Disposed"))
        session = null

        // Cancel all remaining jobs
        scope.cancel()
    }
}