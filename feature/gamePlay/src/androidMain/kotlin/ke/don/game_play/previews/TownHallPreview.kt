/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.previews

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Role
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.player.screens.PlayerTownHall
import ke.don.game_play.shared.SharedTownHall
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@DevicePreviews
@Composable
fun TownHallPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    val players = FakeData.players
    val accuser = FakeData.accuser
    val accused = FakeData.accused
    val seconder = FakeData.seconder
    val announcements = FakeData.announcements

    DevicePreviewContainer(theme) {
        ScaffoldToken(
            title = "Town Hall",
            navigationIcon = NavigationIcon.Back {},
        ) {
            SharedTownHall(
                accuser = accuser,
                accused = accused,
                seconder = seconder,
                myPlayerId = seconder.id,
                onSecond = {},
                players = players,
                onSelectPlayer = {},
                onShowRules = {},
                exoneratePlayer = {},
                proceed = {},
                announcements = announcements,
                knownIdentity = emptyList(),
                revealDeaths = false,
                onDismiss = {},
                lastSaved = null,
                lastKilled = emptyList(),
                isModerator = false,
            )
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun PlayerTownHallPreview(
    @PreviewParameter(SleepRoleProvider::class) role: Role
) {
    val players = FakeData.players
    val gameState = FakeData.townHallGameState
    val currentPlayer = FakeData.currentPlayer(role)
    val playerState = PlayerState(selectedId = "1")

    DevicePreviewContainer {
        ScaffoldToken(
            title = "Town Hall Modal - ${role.name}",
            navigationIcon = NavigationIcon.Back {},
        ) {
            PlayerTownHall(
                gameState = gameState,
                myPlayer = currentPlayer,
                players = players,
                onEvent = {},
                playerState = playerState,
            )
        }
    }
}
