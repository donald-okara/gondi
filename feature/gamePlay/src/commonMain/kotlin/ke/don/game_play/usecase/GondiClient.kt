package ke.don.game_play.usecase

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientMessage
import ke.don.domain.gameplay.server.ServerMessage
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.remote.server.ClientObject.client
import ke.don.utils.Logger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

class GondiClient(): ScreenModel {

    val logger = Logger("GondiClient")

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes.asStateFlow()

    private var session: DefaultClientWebSocketSession? = null

    fun connect(host: String, port: Int) = screenModelScope.launch {
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
                            is ServerUpdate.Error ->{
                                logger.error("❌ ${update.message}")

                                Matcha.error(
                                    title = "Error",
                                    description = update.message
                                )
                            }
                            is ServerUpdate.Announcement -> {
                                logger.info("📣 ${update.message}")

                                Matcha.info(
                                    title = "Announcement",
                                    description = update.message
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    fun sendIntent(intent: PlayerIntent) {
        screenModelScope.launch {
            val message = ServerMessage.PlayerIntentMsg(intent)
            session?.send(Frame.Text(Json.encodeToString(message)))
                ?: logger.error("⚠️ Not connected to server")
        }

    }

    fun disconnect() {
        screenModelScope.launch {
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client exit"))
            session = null
        }
    }

    fun startPing() = screenModelScope.launch {
        while (true) {
            delay(10_000)
            session?.send(Frame.Text(Json.encodeToString(ClientMessage.Ping)))
        }
    }
}
