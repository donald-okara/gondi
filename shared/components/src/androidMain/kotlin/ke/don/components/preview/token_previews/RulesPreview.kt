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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.guide.RolesList
import ke.don.components.guide.RulesContent
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun RolesPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        ScaffoldToken {
            RolesList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun RulesContentPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        ScaffoldToken {
            RulesContent()
        }
    }
}
