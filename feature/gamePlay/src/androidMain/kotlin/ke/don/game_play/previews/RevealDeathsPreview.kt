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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.shared.components.RevealDeathsComponent
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

@DevicePreviews
@Composable
fun RevealDeathsPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    val deadPlayers = remember {
        listOf(
            Player(
                id = "1",
                name = "Matt Foley",
                role = Role.VILLAGER,
                Avatar.Alexander,
                background = AvatarBackground.PURPLE_LILAC,
            ),
            Player(
                id = "2",
                name = "Stefon Zelesky",
                role = Role.VILLAGER,
                Avatar.Christian,
                background = AvatarBackground.PINK_HOT,
            ),
        )
    }
    val savedPlayer =
        Player(
            id = "3",
            name = "David S. Pumpkins",
            role = Role.GONDI,
            Avatar.Amaya,
            background = AvatarBackground.YELLOW_BANANA,
        )
    DevicePreviewContainer(theme) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.size(600.dp),
                contentAlignment = Alignment.Center,
            ) {
                RevealDeathsComponent(
                    savedPlayer = savedPlayer,
                    currentPhase = GamePhase.SLEEP,
                    strings = RevealDeathsStrings(
                        nightResultsTitle = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS),
                        courtRulingTitle = stringResource(Resources.Strings.GamePlay.COURT_RULING),
                        nightResultsDescription = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS_DESCRIPTION),
                        courtRulingDescription = stringResource(Resources.Strings.GamePlay.COURT_RULING_DESCRIPTION),
                        killedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.KILLED_PLAYER),
                        savedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.SAVED_PLAYER),
                        eliminatedPlayerMessage = { role -> Resources.Strings.GamePlay.eliminatedPlayer(role) },
                        savedBySaviourMessage = { saviour -> Resources.Strings.GamePlay.savedBySaviour(saviour) },
                        courtRulingText = stringResource(Resources.Strings.GamePlay.COURT_RULING),
                        theDoctorText = stringResource(Resources.Strings.GamePlay.THE_DOCTOR),
                    ),
                )
            }
        }
    }
}
