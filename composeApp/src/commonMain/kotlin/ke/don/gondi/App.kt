/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import ke.don.components.card.Toast
import ke.don.design.theme.AppTheme
import ke.don.domain.datastore.Theme
import ke.don.gondi.navigation.AuthScreen
import ke.don.koffee.annotations.ExperimentalKoffeeApi
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.ui.KoffeeBar
import ke.don.local.datastore.ThemeRepository
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import org.koin.compose.getKoin

/**
 * Root composable that applies the app theme and hosts the UI with a configured KoffeeBar for toasts.
 *
 * with a slide-up animation and a maximum of 3 visible toasts, and composes a centered Surface containing the
 * greeting text and a primary ButtonToken that triggers a success toast via Matcha.success when clicked.
 */

@OptIn(ExperimentalKoffeeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
    val koin = getKoin()
    val themeRepository = koin.get<ThemeRepository>()
    val theme by themeRepository.theme.collectAsState(
        initial = Theme.System,
    )

    AppTheme(
        theme = theme ?: Theme.System,
    ) {
        val koffeeConfig = KoffeeDefaults.config.copy(
            layout = { Toast(data = it) },
            position = ToastPosition.BottomEnd,
            maxVisibleToasts = 3,
            animationStyle = ToastAnimation.SlideUp,
        )
        KoffeeBar(
            config = koffeeConfig,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                val startDestination = AuthScreen()
                Navigator(startDestination) { navigator ->
                    SharedTransitionLayout {
                        AnimatedContent(
                            targetState = navigator.lastItem,
                            transitionSpec = {
                                if (navigator.lastEvent == StackEvent.Pop) {
                                    (scaleIn(initialScale = 1.2f) + fadeIn()) togetherWith
                                        (scaleOut(targetScale = 0.8f) + fadeOut())
                                } else {
                                    (scaleIn(initialScale = 0.8f) + fadeIn()) togetherWith
                                        (scaleOut(targetScale = 1.2f) + fadeOut())
                                }
                            },
                        ) { screen ->
                            CompositionLocalProvider(
                                LocalSharedScope provides this@SharedTransitionLayout,
                                LocalVisibilityScope provides this@AnimatedContent,
                            ) {
                                screen.Content()
                            }
                        }
                    }
                }
            }
        }
    }
}
