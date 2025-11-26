package ke.don.game_play.player.useCases

import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.websocket.Frame
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientUpdate
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GamePlayerController(
    private val clientManager: GameClientManager
) {
    suspend fun sendIntent(intent: PlayerIntent) {
        val message = ClientUpdate.PlayerIntentMsg(intent)
        clientManager.send(message)
    }
}