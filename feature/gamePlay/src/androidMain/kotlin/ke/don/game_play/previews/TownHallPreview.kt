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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.shared.SharedTownHall
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@DevicePreviews
@Composable
fun TownHallPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    val players = remember {
        listOf(
            Player(id = "1", name = "Matt Foley", role = Role.VILLAGER, Avatar.Alexander, background = AvatarBackground.PURPLE_LILAC),
            Player(id = "2", name = "Stefon Zelesky", role = Role.VILLAGER, Avatar.Christian, background = AvatarBackground.PINK_HOT),
            Player(id = "3", name = "David S. Pumpkins", role = Role.GONDI, Avatar.Amaya, background = AvatarBackground.YELLOW_BANANA),
            Player(id = "4", name = "Roseanne Roseannadanna", role = Role.DETECTIVE, Avatar.Aidan, background = AvatarBackground.GREEN_LEAFY),
            Player(id = "5", name = "Todd O'Connor", role = Role.VILLAGER, Avatar.Kimberly, background = AvatarBackground.ORANGE_CORAL),
            Player(id = "6", name = "Pat O'Neill", role = Role.VILLAGER, Avatar.George, background = AvatarBackground.PURPLE_AMETHYST),
            Player(id = "7", name = "Hans", role = Role.VILLAGER, Avatar.Jocelyn, background = AvatarBackground.GREEN_MINTY),
            Player(id = "8", name = "Franz", role = Role.MODERATOR, Avatar.Jameson, background = AvatarBackground.YELLOW_GOLDEN),
        )
    }

    val accuser = Player(id = "1", name = "Matt Foley", role = Role.VILLAGER, Avatar.Alexander, background = AvatarBackground.PURPLE_LILAC)
    val accused = Player(id = "12", name = "Stefon Zelesky", role = Role.VILLAGER, Avatar.Christian, background = AvatarBackground.ORANGE_CORAL)
    val seconder = Player(id = "5", name = "Todd O'Connor", role = Role.VILLAGER, Avatar.Kimberly, background = AvatarBackground.ORANGE_CORAL)

    var seconderUi by remember {
        mutableStateOf<Player?>(null)
    }
    var accusedUi by remember {
        mutableStateOf<Player?>(accused)
    }

    val announcements = // emptyList()
        listOf(
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
            "Matt Foley just joined" to Clock.System.now(),
        )

    DevicePreviewContainer(theme) {
        ScaffoldToken(
            title = "Town Hall",
            navigationIcon = NavigationIcon.Back {},
        ) {
            SharedTownHall(
                accuser = accuser,
                accused = accusedUi,
                seconder = seconderUi,
                myPlayerId = seconder.id,
                onSecond = {
                    seconderUi = if (seconderUi == null) seconder else null
                },
                players = players,
                onSelectPlayer = {},
                onShowRules = {},
                exoneratePlayer = {
                    accusedUi = null
                },
                goToCourt = {
                    accusedUi = accused
                },
                announcements = announcements,
                knownIdentity = emptyList(),
                isModerator = false,
            )
        }
    }
}
