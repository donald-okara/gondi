package ke.don.game_play.moderator.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

@Composable
fun EmptySlot(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(160.dp),
        shape = RoundedCornerShape(Theme.spacing.medium),
        tonalElevation = Theme.spacing.small,
        border = BorderStroke(
            width = Theme.spacing.tiny,
            color = Theme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
    ){
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Empty Slot",
                tint = Theme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.size(Theme.spacing.extraLarge)
            )
        }
    }
}
