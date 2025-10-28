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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.background.GradientBackground
import ke.don.components.button.ButtonShowcase
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews

@DevicePreviews
@Composable
fun BackgroundPreview(
    @PreviewParameter(DarkModeProvider::class) isDarkTheme: Boolean,
) {
    DevicePreviewContainer(isDarkTheme) {
        GradientBackground(
            modifier = Modifier.fillMaxSize(),
        ) {
            ButtonShowcase()
        }
    }
}
