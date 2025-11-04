package ke.don.remote.gameplay

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientMessage
import ke.don.domain.gameplay.server.ServerMessage
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.utils.Logger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GondiClient(
    private val playerId: String,
    private val host: String,
    private val port: Int
) {
    private val client = HttpClient {
        install(WebSockets)
    }

    val logger = Logger("GondiClient")

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes.asStateFlow()

    private var session: DefaultClientWebSocketSession? = null

    suspend fun connect() = coroutineScope {
        client.webSocket("ws://$host:$port/game") {
            session = this
            logger.info("Connected ✅")

            launch {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val json = frame.readText()
                        val update = Json.decodeFromString<ServerUpdate>(json)
                        when (update) {
                            is ServerUpdate.GameStateSnapshot -> _gameState.value = update.state
                            is ServerUpdate.PlayersSnapshot -> _players.value = update.players
                            is ServerUpdate.VotesSnapshot -> _votes.value = update.votes
                            is ServerUpdate.Error -> logger.error("❌ ${update.message}")
                            is ServerUpdate.Announcement -> logger.info("📣 ${update.message}")
                        }
                    }
                }
            }
        }
    }


    suspend fun sendIntent(intent: PlayerIntent) {
        val message = ServerMessage.PlayerIntentMsg(intent)
        session?.send(Frame.Text(Json.encodeToString(message)))
            ?: logger.error("⚠️ Not connected to server")
    }

    suspend fun disconnect() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client exit"))
        session = null
    }

    suspend fun startPing() = coroutineScope {
        launch {
            while (true) {
                delay(10_000)
                session?.send(Frame.Text(Json.encodeToString(ClientMessage.Ping)))
            }
        }
    }

}
