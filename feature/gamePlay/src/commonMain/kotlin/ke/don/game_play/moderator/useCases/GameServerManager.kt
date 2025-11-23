package ke.don.game_play.moderator.useCases

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.local.datastore.ProfileStore
import ke.don.utils.result.LocalError
import ke.don.utils.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameServerManager(
    private val server: LocalServer
){
    fun startServer(
        gameIdentity: GameIdentity,
        newGame: GameState,
        scope: CoroutineScope,
        hostPlayer: Player
    ) {
        scope.launch {
            server.stop()
            server.start(gameIdentity)
            server.handleModeratorCommand(
                gameIdentity.id,
                ModeratorCommand.CreateGame(gameIdentity.id, newGame, hostPlayer)
            )
        }
    }

    fun stopServer(
        scope: CoroutineScope
    ){
        scope.launch {
            runCatching { server.stop() }.onFailure { it.printStackTrace() }
        }
    }
}