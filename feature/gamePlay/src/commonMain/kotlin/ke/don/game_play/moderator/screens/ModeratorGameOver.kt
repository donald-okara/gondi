package ke.don.game_play.moderator.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.shared.SharedGameOver

@Composable
fun ModeratorGameOver(
    modifier: Modifier = Modifier,
    myPlayer: Player,
    gameState: GameState,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit
) {
    val winningFaction = remember{ gameState.winners } ?: return

    SharedGameOver(
        modifier = modifier,
        isModerator = false,
        players = players,
        myPlayer = myPlayer,
        winnerFaction = winningFaction,
        playAgain = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameId = gameState.id,
                        phase = GamePhase.LOBBY
                    )
                )
            )
        }
    )
}