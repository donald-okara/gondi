package ke.don.game_play.player.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.shared.SharedGameOver

@Composable
fun PlayerGameOver(
    modifier: Modifier = Modifier,
    myPlayer: Player,
    gameState: GameState,
    players: List<Player>,
) {
    val winningFaction = remember{ gameState.winners } ?: return

    SharedGameOver(
        modifier = modifier,
        isModerator = false,
        players = players,
        myPlayer = myPlayer,
        winnerFaction = winningFaction,
        playAgain = {}
    )
}