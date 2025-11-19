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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.home.model.HomeIntentHandler
import ke.don.home.model.HomeModel
import ke.don.home.screens.HomeContent

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeModel = koinScreenModel<HomeModel>()
        val state by homeModel.uiState.collectAsState()
        val onEvent = homeModel::onEvent

        fun extendedOnEvent(intent: HomeIntentHandler){
            when(intent){
                is HomeIntentHandler.NavigateToRules ->
                    navigator.push(RulesScreen())
                is HomeIntentHandler.NavigateToNewGame ->
                {}
                is HomeIntentHandler.NavigateToGame ->
                {}
                is HomeIntentHandler.NavigateToEdit ->
                    navigator.push(EditProfileScreen())
                else -> onEvent(intent)
            }
        }
        LaunchedEffect(homeModel){
            onEvent(HomeIntentHandler.DiscoverGames)
        }
        HomeContent(
            state = state,
            onEvent = ::extendedOnEvent,
            navigateToAuth = { navigator.push(AuthScreen()) }
        )
    }
}
