package ke.don.game_play.player.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.websocket.Frame
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientUpdate
import ke.don.domain.gameplay.server.ServerId
import ke.don.game_play.player.useCases.GameClientManager
import ke.don.game_play.player.useCases.GameClientState
import ke.don.game_play.player.useCases.GamePlayerController
import ke.don.utils.Logger
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GondiClient(
    private val clientState: GameClientState,
    private val clientManager: GameClientManager,
    private val controller: GamePlayerController
) : ScreenModel {
    val logger = Logger("GondiClient")

    val currentPlayer =  clientState.currentPlayer
    val gameState = clientState.gameState
    val players = clientState.players
    val votes = clientState.votes
    val playerState = clientState.playerState


    fun onEvent(intent: PlayerHandler){
        when(intent){
            is PlayerHandler.Connect -> connect(intent.serverId)
            is PlayerHandler.Send -> sendIntent(intent.message)
            PlayerHandler.ShowLeaveDialog -> clientState.updatePlayerState { it.copy(showLeaveGame = true) }
        }
    }

    fun connect(serverId: ServerId) {
        screenModelScope.launch {
            logger.info(
                "Player: ${currentPlayer.first()?.name} connecting to ${serverId.first}:${serverId.second}"
            )
            clientManager.connect(serverId).onFailure { error ->
                Matcha.showErrorToast(
                    title = "Connection failed",
                    message = error.message,
                    retryAction = { connect(serverId) }
                )
            }

        }
    }

    fun sendIntent(intent: PlayerIntent){
        screenModelScope.launch {
            controller.sendIntent(intent)
        }
    }

    fun dispose(){
        screenModelScope.launch{
            clientManager.dispose()
            clientState.clearState()
        }
    }
}