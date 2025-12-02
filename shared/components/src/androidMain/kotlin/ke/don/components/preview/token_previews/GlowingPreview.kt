/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.AppTheme
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

@DevicePreviews
@Composable
fun PlayersPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        AppTheme(theme) {
            val players = remember {
                List(Avatar.entries.size) { index ->
                    Player(
                        name = Avatar.entries[index].name,
                        role = Role.entries[index % Role.entries.size],
                        avatar = Avatar.entries[index],
                        background = AvatarBackground.entries[index % AvatarBackground.entries.size],
                        isAlive = true,
                    )
                }
            }

            val actionTypes = remember {
                ActionType.entries
            }

            var selectedColor by remember { mutableStateOf(ActionType.ACCUSE) }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(actionTypes.size) { index ->
                        val actionType = actionTypes[index]
                        PlayerItem(
                            actionType = actionType,
                            onClick = { selectedColor = actionType },
                            isSelected = selectedColor == actionType,
                            showRole = true,
                            enabled = true,
                            player = players[index % players.size],
                        )
                    }
                }
            }
        }
    }
}
