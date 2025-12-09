package ke.don.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.VersionCompatibility
import ke.don.home.model.HomeIntentHandler

@Composable
fun CompatibilityDialog(
    modifier: Modifier = Modifier,
    versionCompatibility: VersionCompatibility,
    onEvent: (HomeIntentHandler) -> Unit,
    updateGame: () -> Unit,
    game: GameIdentity
) {
    when(versionCompatibility){
        VersionCompatibility.COMPATIBLE -> return
        VersionCompatibility.PARTIALLY_COMPATIBLE -> {
            ConfirmationDialogToken(
                modifier = modifier,
                icon = Icons.Outlined.Warning,
                title = "Mismatched Versions",
                message = "You're trying to join ${game.gameName}, but your game version doesn't fully match the host's. You can still join, but be aware that:",
                checklist = listOf(
                    "Game rules might differ slightly.",
                    "Some avatars or backgrounds may not appear correctly.",
                    "You might experience unexpected behavior."
                ),
                dialogType = ComponentType.Warning,
                secondaryAction = { updateGame() },
                secondaryText = "Update Game",
                onConfirm = { onEvent(HomeIntentHandler.NavigateToGame(game.serviceHost to game.servicePort)) },
                onDismiss = {
                    onEvent(HomeIntentHandler.SelectGame())
                    onEvent(HomeIntentHandler.ShowVersionMismatch())
                }
            )
        }
        VersionCompatibility.INCOMPATIBLE -> {
            ConfirmationDialogToken(
                modifier = modifier,
                icon = Icons.Outlined.Warning,
                title = "Incompatible Version",
                message = "Your game version is incompatible with ${game.gameName}. To join, you'll need to update your game. Please ask the host to update theirs as well if the problem persists.",
                dialogType = ComponentType.Warning,
                onConfirm = { updateGame },
                onDismiss = {
                    onEvent(HomeIntentHandler.SelectGame())
                    onEvent(HomeIntentHandler.ShowVersionMismatch())
                }
            )
        }
        VersionCompatibility.INVALID -> return
    }
}