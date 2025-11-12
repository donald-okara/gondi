/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.authentication.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.authentication.components.SignInScreen
import ke.don.authentication.components.SplashScreen
import ke.don.authentication.model.AuthEvent
import ke.don.authentication.model.AuthModel
import ke.don.authentication.model.StartupPhase
import ke.don.components.background.GradientBackground
import ke.don.components.empty_state.EmptyScreen
import ke.don.components.helpers.ObserveAsEvent
import ke.don.home.screens.HomeScreen
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import org.koin.compose.getKoin

class AuthenticationScreen : Screen {
    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var startupPhase by rememberSaveable {
            mutableStateOf(StartupPhase.Splash)
        }
        val koin = getKoin()
        val screenModel = koinScreenModel<AuthModel>()

        val state by screenModel.uiState.collectAsState()

        ObserveAsEvent(screenModel.events) { event ->
            when (event) {
                is AuthEvent.SwitchSignIn -> startupPhase = StartupPhase.OnBoarding
                is AuthEvent.SwitchMain -> navigator.replaceAll(HomeScreen()) // Replace with actual navigation
                is AuthEvent.SwitchProfile -> startupPhase = StartupPhase.Profile // Replace with actual navigation
                else -> Unit
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
                        val next = targetState.ordinal > initialState.ordinal
                        if (next) {
                            (scaleIn(initialScale = 1.2f) + fadeIn()) togetherWith
                                (scaleOut(targetScale = 0.8f) + fadeOut())
                        } else {
                            (scaleIn(initialScale = 0.8f) + fadeIn()) togetherWith
                                (scaleOut(targetScale = 1.2f) + fadeOut())
                        }
                    },
                    label = "StartupPhaseTransition",
                ) { phase ->
                    CompositionLocalProvider(
                        LocalSharedScope provides this@SharedTransitionLayout,
                        LocalVisibilityScope provides this@AnimatedContent,
                    ) {
                        when (phase) {
                            StartupPhase.Splash -> SplashScreen()
                            StartupPhase.OnBoarding -> SignInScreen(
                                state = state,
                                onEvent = screenModel::handleAction,
                            )
                            StartupPhase.Main -> EmptyScreen(
                                icon = Icons.Outlined.Lock,
                                title = "Main",
                                description = "Screen is in development",
                            )

                            StartupPhase.Profile -> EmptyScreen(
                                icon = Icons.Outlined.Lock,
                                title = "Profile",
                                description = "Screen is in development",
                            )
                        }
                    }
                }
            }
        }
    }
}
