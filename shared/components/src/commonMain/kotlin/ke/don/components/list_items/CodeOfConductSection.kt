package ke.don.components.list_items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NoSim
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

@Composable
fun CodeOfConductSection(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = Theme.shapes.medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        tonalElevation = Theme.spacing.small,
        border = BorderStroke(
            width = Theme.spacing.tiny,
            color = Theme.colorScheme.primary.copy(alpha = 0.5f),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(Theme.spacing.large)
                )

                Spacer(Modifier.width(Theme.spacing.medium))

                Column {
                    Text(
                        text = "Code of Conduct",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "For a fair and fun game, please keep these rules in mind.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(Theme.spacing.large))

            val minSize = 240.dp
            val spacing = MaterialTheme.spacing.medium

            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val cols = ((maxWidth - spacing * 2) / minSize)
                    .toInt()
                    .coerceIn(1, 3) // min 1, max 3 columns

                val itemWidth = (maxWidth - spacing * (cols - 1)) / cols

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.NoSim,
                        title = "No peeking!",
                        description = "Keep your eyes on your own screen. What's on other players' phones is their secret."
                    )

                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.VisibilityOff,
                        title = "Keep it secret",
                        description = "Your role is for your eyes only. Don't reveal your screen to others."
                    )

                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.SentimentSatisfied,
                        title = "Play nice",
                        description = "Good sportsmanship makes the game better for everyone. Let's all have a great time!"
                    )
                }
            }

        }
    }
}


@Composable
private fun ConductItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(0.9f)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(Theme.spacing.large)
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
