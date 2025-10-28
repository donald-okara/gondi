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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.card.Toast
import ke.don.components.helpers.Matcha
import ke.don.design.theme.AppTheme
import ke.don.koffee.annotations.ExperimentalKoffeeApi
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.ui.KoffeeBar
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Root composable that applies the app theme and hosts the UI with a configured KoffeeBar for toasts.
 *
 * Uses Greeting().greet() for the displayed greeting, configures Koffee to render Toasts at the bottom-end
 * with a slide-up animation and a maximum of 3 visible toasts, and composes a centered Surface containing the
 * greeting text and a primary ButtonToken that triggers a success toast via Matcha.success when clicked.
 */
@OptIn(ExperimentalKoffeeApi::class)
@Composable
@Preview
fun App() {
    val greeting = Greeting().greet()
    AppTheme {
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        Text(greeting)

                        ButtonToken(
                            buttonType = ComponentType.Primary,
                            onClick = { Matcha.success("Hello. It's me") },
                        ) {
                            Text("Click Me")
                        }
                    }
                }
            }
        }
    }
}