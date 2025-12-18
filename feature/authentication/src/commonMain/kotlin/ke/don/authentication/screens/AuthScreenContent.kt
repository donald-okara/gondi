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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.authentication.components.SignInScreen
import ke.don.authentication.components.SplashScreen
import ke.don.authentication.model.AuthModel
import ke.don.authentication.model.AuthState
import ke.don.authentication.model.StartupPhase
import ke.don.components.background.GradientBackground
import ke.don.components.empty_state.EmptyState
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.design.theme.spacing
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import ke.don.resources.Resources.Strings.Authentication.HAVE_A_COFFEE
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AuthScreenContent(
    modifier: Modifier = Modifier,
    startupPhase: StartupPhase,
    state: AuthState,
    screenModel: AuthModel,
) {
    GradientBackground(
        modifier = modifier.fillMaxSize(),
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

                        StartupPhase.Main -> {}

                        StartupPhase.Profile -> ProfileSyncScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSyncScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier.width(MaterialTheme.spacing.largeScreenSize),
            contentAlignment = Alignment.Center,
        ) {
            FancyLoadingIndicator(loading = true)
        }
        Text(
            stringResource(HAVE_A_COFFEE)
        )
    }
}
