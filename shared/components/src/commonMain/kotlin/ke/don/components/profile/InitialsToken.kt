package ke.don.components.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.domain.model.Profile
import ke.don.resources.color
import ke.don.resources.onColorWithOverlay
import ke.don.utils.getInitials

@Composable
fun InitialsToken(
    profile: Profile,
    isHero: Boolean = false,
    modifier: Modifier = Modifier,
) {
    // Animate size
    val animatedSize by animateDpAsState(
        targetValue = if (isHero) 96.dp else 32.dp,
        label = "size",
    )

    // Animate font scaling (we’ll use this to interpolate text size)
    val animatedFontScale by animateFloatAsState(
        targetValue = if (isHero) 1.8f else 1f,
        label = "fontScale",
    )

    val baseTextStyle = if (isHero) {
        MaterialTheme.typography.headlineMedium
    } else {
        MaterialTheme.typography.labelMedium
    }

    Surface(
        color = Color.Transparent,
        shape = if (isHero) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
        tonalElevation = if (isHero) 3.dp else 1.dp,
        modifier = modifier.size(animatedSize),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = profile.name.getInitials(),
                style = baseTextStyle.copy(
                    fontWeight = FontWeight.Bold,
                    color = profile.background.color().onColorWithOverlay(),
                    fontSize = baseTextStyle.fontSize * animatedFontScale,
                ),
                maxLines = 1,
            )
        }
    }
}