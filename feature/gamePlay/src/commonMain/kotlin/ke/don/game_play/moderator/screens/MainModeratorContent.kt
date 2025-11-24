package ke.don.game_play.moderator.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.utils.capitaliseFirst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModeratorContent(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    hostPlayer: Player?,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (ModeratorHandler) -> Unit,
    onBack: () -> Unit
) {
    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back { onEvent(ModeratorHandler.ShowLeaveDialog) },
        title = gameState?.phase?.let { phase ->
            "${phase.name.capitaliseFirst()} for ${gameState.name}"
        } ?: "New game"
    ) {
        ContentSwitcher(
            moderatorState = moderatorState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            hostPlayer = hostPlayer
        )
    }

    if (moderatorState.showLeaveGame){
        ConfirmationDialogToken(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Leave Game?",
            message = "You are about to leave the game you should understand that:",
            dialogType = ComponentType.Warning,
            checklist = listOf(
                "This game will be terminated and all progress will be lost."
            ),
            onConfirm = onBack,
            onDismiss = { onEvent(ModeratorHandler.ShowLeaveDialog) },
        )
    }

}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    players: List<Player>,
    hostPlayer: Player?,
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
                ModeratorLobby(
                    modifier = modifier,
                    gameState = gameState,
                    players = players,
                    onEvent = onEvent,
                    myPlayerId = hostPlayer?.id
                )
            }
            GamePhase.SLEEP -> {}
            GamePhase.TOWN_HALL -> {}
            GamePhase.COURT -> {}
            GamePhase.GAME_OVER -> {}
        }
    }
}