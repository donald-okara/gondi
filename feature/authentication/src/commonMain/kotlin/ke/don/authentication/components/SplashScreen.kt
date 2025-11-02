package ke.don.authentication.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import ke.don.resources.Resources
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    val sharedScope = LocalSharedScope.current
    val visibilityScope = LocalVisibilityScope.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        with(sharedScope) {
            with(visibilityScope) {
                Image(
                    painter = painterResource(Resources.Images.LOGO),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "app_logo"),
                            animatedVisibilityScope = visibilityScope,
                        )
                )
            }
        }
    }
}