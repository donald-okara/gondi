/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.domain.table.Profile
import ke.don.home.model.HomeIntentHandler
import ke.don.home.model.HomeState
import ke.don.home.screens.HomeContent
import ke.don.utils.result.ReadStatus
import kotlinx.coroutines.delay

@Composable
fun HomeShowcase(
    modifier: Modifier = Modifier,
) {
    val games = listOf<GameIdentity>(
        GameIdentity(
            id = "1",
            gameName = "Cats vs Cucumbers",
            moderatorName = "General Whiskers",
            moderatorAvatar = Avatar.Alexander,
            moderatorAvatarBackground = AvatarBackground.PURPLE_LILAC,
        ),
        GameIdentity(
            id = "2",
            gameName = "Extreme Naptime Championship",
            moderatorName = "SleepyHead",
            moderatorAvatar = Avatar.Adrian,
            moderatorAvatarBackground = AvatarBackground.GREEN_EMERALD,
        ),
        GameIdentity(
            id = "3",
            gameName = "Who Stole My Sandwich?",
            moderatorName = "DetectiveNomNom",
            moderatorAvatar = Avatar.Amaya,
            moderatorAvatarBackground = AvatarBackground.YELLOW_BANANA,
        ),
        GameIdentity(
            id = "4",
            serviceHost = "192.168.1.101",
            gameName = "Synchronized Sighing",
            moderatorName = "DramaKing_42",
            moderatorAvatar = Avatar.Christian,
            moderatorAvatarBackground = AvatarBackground.PURPLE_LILAC,
        ),
    )

    var readStatus by remember { mutableStateOf<ReadStatus>(ReadStatus.Success) }

    // Simulate pull progression when refreshing
    LaunchedEffect(readStatus) {
        when (readStatus) {
            is ReadStatus.Refreshing -> {
                // Animate progress up
                val duration = 400L
                val steps = 20
                repeat(steps) {
                    delay(duration / steps)
                }

                // Hold for effect
                delay(1200)

                // Reset
                readStatus = ReadStatus.Success
            }
            else -> {}
        }
    }
    var state by remember(
        games,
        readStatus,
    ) {
        mutableStateOf(HomeState(games = games, readStatus = readStatus, profile = Profile(username = "Don", background = AvatarBackground.PURPLE_LILAC, avatar = Avatar.Alexander)))
    }
    fun handleIntent(intent: HomeIntentHandler) {
        when (intent) {
            is HomeIntentHandler.Refresh -> readStatus = ReadStatus.Refreshing
            else -> {}
        }
    }

    HomeContent(
        modifier = modifier,
        state = state,
        onEvent = ::handleIntent,
    )
}

@DevicePreviews
@Composable
fun HomeShowcasePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) { HomeShowcase() }
}
