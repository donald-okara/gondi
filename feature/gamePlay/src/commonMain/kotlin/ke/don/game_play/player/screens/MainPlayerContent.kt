package ke.don.game_play.player.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.design.theme.spacing
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.player.components.PlayerLobby
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.utils.capitaliseFirst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPlayerContent(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    gameState: GameState? = null,
    currentPlayer: Player?,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (PlayerHandler) -> Unit,
    onBack: () -> Unit,
) {
    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back { onEvent(PlayerHandler.ShowLeaveDialog) },
        title = gameState?.phase?.let { phase ->
            "${phase.name.capitaliseFirst()} for ${gameState.name}"
        } ?: "Connecting ...",
    ) {
        ContentSwitcher(
            playerState = playerState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            currentPlayer = currentPlayer,
        )
    }

    if (playerState.showLeaveGame) {
        ConfirmationDialogToken(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Leave Game?",
            message = "You are about to leave the game you should understand that:",
            dialogType = ComponentType.Warning,
            checklist = listOf(
                "Your progress will be lost",
                "You cannot come back to the game",
            ),
            onConfirm = onBack,
            onDismiss = { onEvent(PlayerHandler.ShowLeaveDialog) },
        )
    }
}

@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    gameState: GameState? = null,
    players: List<Player>,
    currentPlayer: Player?,
    votes: List<Vote>,
    onEvent: (PlayerHandler) -> Unit,
) {
    AnimatedContent(
        targetState = gameState?.phase,
        label = "Game State",
    ) { phase ->
        when (phase) {
            null -> {
                LoadingState(
                    modifier = modifier
                )
            }
            GamePhase.LOBBY -> {
                PlayerLobby(
                    modifier = modifier,
                    players = players,
                    myPlayerId = currentPlayer?.id,
                    gameState = gameState
                )
            }
            GamePhase.SLEEP -> {}
            GamePhase.TOWN_HALL -> {}
            GamePhase.COURT -> {}
            GamePhase.GAME_OVER -> {}
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.width(MaterialTheme.spacing.largeScreenSize),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically)
            ){
                FancyLoadingIndicator(loading = true)
                Text(
                    text = "Connecting...",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
