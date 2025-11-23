package ke.don.components.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.GlowingSelectableSurface
import ke.don.design.theme.AppTheme
import ke.don.design.theme.Theme
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Faction
import ke.don.domain.state.Player
import ke.don.resources.Resources
import ke.don.resources.color
import ke.don.resources.painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.graphics.ColorMatrix // <-- Add this import
import androidx.compose.ui.graphics.RenderEffect // <-- Add this import
import ke.don.design.theme.spacing

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    actionType: ActionType,
    onClick: () -> Unit,
    showRole: Boolean = false,
    isSelected: Boolean,
    player: Player,
) {
    // Animate alpha for the entire item if the player is not alive
    val contentAlpha by animateFloatAsState(
        targetValue = if (player.isAlive) 1.0f else 0.5f,
        animationSpec = tween(300),
        label = "playerAlpha"
    )

    val color by animateColorAsState(
        targetValue = if (isSelected) actionType.color else player.background.color(),
        animationSpec = tween(300),
    )
    GlowingSelectableSurface(
        modifier = modifier.graphicsLayer { alpha = contentAlpha },
        onClick = onClick,
        selected = isSelected,
        enabled = player.isAlive,
        glowingColor = color,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avatar Box
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(player.background.color())
            ) {
                // Player Avatar or Initials
                val imageAlpha =
                    if (player.isAlive) 1f else 0.5f // Grayscale effect is handled by layer
                val imageModifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAlpha)

                player.avatar?.painter()?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = player.name,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                } ?: InitialsToken(
                    modifier = imageModifier,
                    profile = player.toProfile(),
                    isHero = true,
                    isSelected = false,
                )

                // Eliminated Overlay
                if (!player.isAlive) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Using the skull icon from your HTML example
                        Icon(
                            painter = painterResource(Resources.Images.ActionIcons.DEAD),
                            contentDescription = "Eliminated",
                            tint = Color.Red.copy(alpha = 0.8f),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            // Player Name
            Text(
                text = player.name,
                style = Theme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Theme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            AnimatedVisibility(
                visible = player.isAlive.not()
            ){
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Theme.colorScheme.errorContainer,
                    contentColor = Theme.colorScheme.onErrorContainer
                ){
                    Text(
                        text = "Eliminated",
                        style = Theme.typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.extraSmall,
                            vertical = MaterialTheme.spacing.tiny
                        )
                    )
                }
            }
            AnimatedVisibility(
                visible = showRole || player.isAlive.not()
            ){
                Text(
                    text = player.role?.name ?: "Waiting",
                    style = Theme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = player.role?.faction?.color ?: Theme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Maps an [ActionType] to a corresponding drawable resource for its icon.
 * This is used to visually represent the action a player is performing or has performed.
 *
 * @return A [DrawableResource] representing the icon for the action, or `null` if the action
 * has no associated icon (e.g., [ActionType.NONE]).
 *
 * NOTE: This property is not currently being used but it is handy to have around. shrinkResources is enabled
 * for release builds.
 */
val ActionType.painter: DrawableResource?
    get() = when(this){
        ActionType.KILL ->Resources.Images.ActionIcons.DEAD
        ActionType.ACCUSE, ActionType.SECOND -> Resources.Images.ActionIcons.ACCUSE
        ActionType.SAVE -> Resources.Images.ActionIcons.SAVE
        ActionType.VOTE_GUILTY -> Resources.Images.ActionIcons.VOTE_GUILTY
        ActionType.INVESTIGATE -> Resources.Images.ActionIcons.INVESTIGATE
        ActionType.VOTE_INNOCENT -> Resources.Images.ActionIcons.VOTE_INNOCENT
        ActionType.NONE -> null
    }

val ActionType.color: Color
    @Composable get() = when(this){
        ActionType.KILL, ActionType.ACCUSE, ActionType.VOTE_GUILTY -> Theme.colorScheme.error
        ActionType.SAVE, ActionType.VOTE_INNOCENT -> AppTheme.extendedColors.success.color
        ActionType.SECOND , ActionType.INVESTIGATE -> AppTheme.extendedColors.warning.color
        ActionType.NONE -> Theme.colorScheme.primary
    }

val Faction.color: Color
    @Composable get() = when(this){
        Faction.GONDI -> Theme.colorScheme.error
        Faction.VILLAGER -> AppTheme.extendedColors.success.color
        Faction.NEUTRAL -> Theme.colorScheme.secondary
    }

