package ke.don.game_play.player.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedLobby
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PlayerLobby(
    modifier: Modifier = Modifier,
    gameState: GameState? = null,
    myPlayerId: String? = null,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    SharedLobby(
        modifier = modifier,
        isModerator = false,
        gameState = gameState,
        myPlayerId = myPlayerId,
        players = players,
        announcements = playerState.announcements,
        onShowRules = {
            onEvent(PlayerHandler.ShowRulesModal)
        }
    )
}