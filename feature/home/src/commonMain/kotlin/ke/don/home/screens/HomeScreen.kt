/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.empty_state.EmptyScreen
import ke.don.components.helpers.Matcha
import ke.don.components.profile.ProfileImageToken
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.table.Avatar
import ke.don.domain.table.Profile

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        ScaffoldToken(
            title = "Gondi",
            actions = {
                ProfileImageToken( // TODO: Temporary until profile is implemented
                    isHero = false,
                    profile = Profile(
                        name = "Donald Isoe",
                        avatar = Avatar.Leo,
                    ),
                )
            },
        ) {
            EmptyScreen(
                title = "Home",
                description = "Screen is in development",
                icon = Icons.Outlined.Home,
            ) {
                ButtonToken(
                    buttonType = ComponentType.Inverse,
                    onClick = {
                        Matcha.info(
                            "Sign out",
                        )
                    },
                ) {
                    Text("Sign out")
                }
            }
        }
    }
}
