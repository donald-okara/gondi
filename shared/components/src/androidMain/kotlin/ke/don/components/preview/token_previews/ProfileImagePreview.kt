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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.profile.ProfileImageToken
import ke.don.domain.datastore.Theme
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.domain.table.Profile

@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
fun ProfilePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    var selectedColor by remember {
        mutableStateOf(AvatarBackground.PURPLE_LILAC)
    }

    // <template>
    DevicePreviewContainer(theme = theme) {
        Column {
            LookaheadScope {
                FlowRow(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                ) {
                    AvatarBackground.entries.forEach {
                        ProfileImageToken(
                            profile = Profile(
                                avatar = Avatar.Christian,
                                username = it.name,
                                background = it,
                            ),
                            onClick = {
                                selectedColor = it
                            },
                            modifier = Modifier.animateBounds(this@LookaheadScope),
                            isSelected = it == selectedColor,
                            isHero = true,
                        )
                    }
                }
            }
            LookaheadScope {
                FlowRow(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AvatarBackground.entries.forEach {
                        ProfileImageToken(
                            profile = Profile(
                                avatar = Avatar.Katherine,
                                username = it.name,
                                background = it,
                            ),
                            modifier = Modifier.animateBounds(this@LookaheadScope),
                            isSelected = it == selectedColor,
                            onClick = { selectedColor = it },
                            isHero = false,
                        )
                    }
                }
            }
        }
    }
}
