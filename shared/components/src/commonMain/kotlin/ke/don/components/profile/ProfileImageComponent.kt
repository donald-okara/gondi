package ke.don.components.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ke.don.domain.model.Profile
import ke.don.resources.color
import ke.don.resources.painter
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileBackground(
    modifier: Modifier = Modifier,
    color: Color,
    isHero: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val animatedSize by animateDpAsState(
        targetValue = if (isHero) 96.dp else 32.dp,
        label = "size",
    )

    Surface(
        color = color,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick ?: {},
        tonalElevation = if (isHero) 3.dp else 1.dp,
        modifier = modifier.size(animatedSize),
    ) {
        content.invoke()
    }
}

@Composable
fun ProfileImageToken(
    modifier: Modifier = Modifier,
    profile: Profile,
    onClick: (() -> Unit)? = null,
    isHero: Boolean
) {
    ProfileBackground(
        isHero = isHero,
        color = profile.background.color()
    ){
        profile.avatar?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(it.painter()),
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .padding(if (isHero) 8.dp else 2.dp)
                        .fillMaxSize()
                )
            }

        } ?: InitialsToken(
            profile = profile,
            isHero = isHero,
            modifier = modifier
        )
    }
}