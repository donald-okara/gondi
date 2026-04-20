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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.gameplay.Role
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.player.screens.PlayerSleep
import kotlin.time.ExperimentalTime

class SleepRoleProvider : PreviewParameterProvider<Role> {
    override val values = sequenceOf(
        Role.GONDI,
        Role.DOCTOR,
        Role.DETECTIVE,
        Role.VILLAGER,
    )
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun PlayerSleepPreview(
    @PreviewParameter(SleepRoleProvider::class) role: Role,
) {
    val players = FakeData.players
    val gameState = FakeData.gameState
    val currentPlayer = FakeData.currentPlayer(role)

    val playerState = PlayerState(
        selectedId = "1", // Show the modal by selecting a player
    )

    DevicePreviewContainer {
        ScaffoldToken(
            title = "Sleep - ${role.name}",
            navigationIcon = NavigationIcon.Back {},
        ) {
            PlayerSleep(
                gameState = gameState,
                myPlayer = currentPlayer,
                players = players,
                onEvent = {},
                playerState = playerState,
            )
        }
    }
}
