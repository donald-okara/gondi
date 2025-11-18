/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.authentication.model.AuthEvent
import ke.don.authentication.model.AuthModel
import ke.don.authentication.model.StartupPhase
import ke.don.authentication.screens.AuthScreenContent
import ke.don.components.helpers.ObserveAsEvent
import ke.don.koffee.domain.Koffee

class AuthScreen : Screen {
    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var startupPhase by rememberSaveable {
            mutableStateOf(StartupPhase.Splash)
        }
        val screenModel = koinScreenModel<AuthModel>()

        val state by screenModel.uiState.collectAsState()

        LaunchedEffect(state.profile) {
            state.profile?.let { profile ->
                if (profile.avatar == null) {
                    navigator.push(OnboardingScreen())
                } else {
                    navigator.replaceAll(HomeScreen())
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                Koffee.dismissAll()
            }
        }

        ObserveAsEvent(screenModel.events) { event ->
            when (event) {
                is AuthEvent.SwitchSignIn -> startupPhase = StartupPhase.OnBoarding
                is AuthEvent.SwitchMain -> navigator.replaceAll(HomeScreen()) // Replace with actual navigation
                is AuthEvent.SwitchProfile -> startupPhase = StartupPhase.Profile
                else -> Unit
            }
        }

        AuthScreenContent(
            startupPhase = startupPhase,
            state = state,
            screenModel = screenModel,
        )
    }
}
