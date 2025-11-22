package ke.don.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.GlowingSelectableSurface
import ke.don.design.theme.AppTheme
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Profile

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    actionType: ActionType,
    onClick: () -> Unit,
    showRole: Boolean = true,
    isSelected: Boolean,
    player: Player
) {
    GlowingSelectableSurface(
        modifier = modifier,
        enabled = player.isAlive,
        onClick = onClick,
        selected = isSelected,
        glowingColor = actionType.color,
    ) {
        Column(
            modifier = Modifier
                .padding(Theme.spacing.medium)
                .widthIn(min = 120.dp, max = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Profile image with subtle border
            ProfileImageToken(
                profile = player.toProfile(),
                isHero = true,
                modifier = Modifier
            )

            // Player name
            Text(
                text = player.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Theme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                color = Theme.colorScheme.onSurface
            )

            // Role or status badge
            if (!player.isAlive || (showRole && player.role != null)) {
                val label = if (!player.isAlive) "DECEASED" else player.role?.name.orEmpty()
                val badgeColor = if (!player.isAlive) Theme.colorScheme.error else player.role?.faction?.color ?: Theme.colorScheme.primary

                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.1f), Theme.shapes.medium)
                        .padding(horizontal = Theme.spacing.small, vertical = Theme.spacing.tiny)
                ) {
                    Text(
                        text = label.uppercase(),
                        style = Theme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = badgeColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
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

