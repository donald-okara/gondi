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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.profile.ProfileImageToken
import ke.don.domain.datastore.Theme
import ke.don.domain.table.AvatarBackground
import ke.don.domain.table.Profile

val profiles = listOf(
    Profile(
        name = "Christian",
    ),
    Profile(
        name = "Jason",
    ),
)

@DevicePreviews
@Composable
fun ProfilePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    // <template>
    DevicePreviewContainer(theme = theme) {
        Column {
            FlowRow(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AvatarBackground.entries.forEach {
                    ProfileImageToken(
                        profile = Profile(
                            name = it.name,
                            background = it,
                        ),
                        isHero = true,
                    )
                }
            }
            FlowRow(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AvatarBackground.entries.forEach {
                    ProfileImageToken(
                        profile = Profile(
                            name = it.name,
                            background = it,
                        ),
                        isHero = false,
                    )
                }
            }
        }

        FlowRow(
            modifier = Modifier.width(500.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarBackground.entries.forEach {
                ProfileImageToken(
                    profile = Profile(
                        name = it.name,
                        background = it,
                    ),
                    isHero = true,
                )
            }
        }
        FlowRow(
            modifier = Modifier.width(500.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarBackground.entries.forEach {
                ProfileImageToken(
                    profile = Profile(
                        name = it.name,
                        background = it,
                    ),
                    isHero = false,
                )
            }
        }
    }
}
