package ke.don.game_play.moderator.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModeratorContent(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (ModeratorHandler) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = remember(gameState?.phase, gameState?.round) { ScrollState(0) }

    ScaffoldToken(
        scrollState = scrollState,
        modifier = modifier,
        navigationIcon = NavigationIcon.Back(onBack),
        title = gameState?.phase?.let{ phase -> "Moderator $phase" } ?: "New game"
    ) {
        ContentSwitcher(
            moderatorState = moderatorState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (ModeratorHandler) -> Unit
) {
    AnimatedContent(
        targetState = gameState?.phase,
        label = "Game State"
    ) { phase ->
        when (phase) {
            null -> {
                CreateGameContent(
                    modifier = modifier,
                    state = moderatorState,
                    onEvent = onEvent
                )
            }
            GamePhase.LOBBY -> {
                LobbyContent()
            }
            GamePhase.SLEEP -> {}
            GamePhase.TOWN_HALL -> {}
            GamePhase.COURT -> {}
            GamePhase.GAME_OVER -> {}
        }
    }
}