package ke.don.authentication.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import io.github.jan.supabase.auth.status.SessionStatus
import ke.don.authentication.components.SignInScreen
import ke.don.authentication.components.SplashScreen
import ke.don.authentication.model.StartupPhase
import ke.don.components.background.GradientBackground
import ke.don.components.empty_state.EmptyScreen
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import kotlinx.coroutines.delay

class AuthenticationScreen: Screen {
    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Content() {
        var startupPhase by remember {
            mutableStateOf(StartupPhase.Splash)
        }
        LaunchedEffect(startupPhase) { //TODO: Move to view model channel
            when (startupPhase) {
                StartupPhase.Splash -> {
                    delay(2000)
                    startupPhase = StartupPhase.OnBoarding
                }
                StartupPhase.OnBoarding -> {
                    Unit
                    // Do nothing
                }
                StartupPhase.Main -> {
                    Unit
                }
            }
        }

        GradientBackground(
            modifier = Modifier.fillMaxSize(),
            accentColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = startupPhase,
                    transitionSpec = {
                        // Direction changes depending on navigation
                        val next = targetState.ordinal > initialState.ordinal
                        if (next) {
                            (scaleIn(initialScale = 1.2f) + fadeIn()) togetherWith
                                    (scaleOut(targetScale = 0.8f) + fadeOut())
                        } else {
                            (scaleIn(initialScale = 0.8f) + fadeIn()) togetherWith
                                    (scaleOut(targetScale = 1.2f) + fadeOut())
                        }
                    },
                    label = "StartupPhaseTransition"
                ) { phase ->
                    CompositionLocalProvider(
                        LocalSharedScope provides this@SharedTransitionLayout,
                        LocalVisibilityScope provides this@AnimatedContent,
                    ) {
                        when (phase) {
                            StartupPhase.Splash -> SplashScreen()
                            StartupPhase.OnBoarding -> SignInScreen()
                            StartupPhase.Main -> EmptyScreen(
                                icon = Icons.Outlined.Lock,
                                title = "Authentication",
                                description = "Screen is in development"
                            )
                        }
                    }

                }

            }


        }

    }
}