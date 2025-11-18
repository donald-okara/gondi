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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.list_items.CodeOfConductSection
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.domain.datastore.Theme

@DevicePreviews
@Composable
fun CodeOfConductPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        Box(modifier = Modifier.fillMaxSize()) { CodeOfConductSection() }
    }
}
