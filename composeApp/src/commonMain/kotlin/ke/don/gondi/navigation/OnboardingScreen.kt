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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.profile.model.EditProfileModel
import ke.don.profile.screens.EditScreenContent
import ke.don.profile.screens.OnBoardingContent

class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val editProfileModel = koinScreenModel<EditProfileModel>()
        val state by editProfileModel.uiState.collectAsState()
        val handleEvent = editProfileModel::onEvent

        val navigator = LocalNavigator.currentOrThrow

        OnBoardingContent(
            state = state,
            handleEvent = handleEvent,
            navigateToMain = { navigator.replaceAll(HomeScreen()) },
        )
    }
}

class EditProfileScreen : Screen {
    @Composable
    override fun Content() {
        val editProfileModel = koinScreenModel<EditProfileModel>()
        val state by editProfileModel.uiState.collectAsState()
        val handleEvent = editProfileModel::onEvent

        val navigator = LocalNavigator.currentOrThrow

        EditScreenContent(
            state = state,
            onEvent = handleEvent,
            back = { navigator.replaceAll(HomeScreen()) },
        )
    }
}
