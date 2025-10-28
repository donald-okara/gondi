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

/**
 * Displays a device-themed preview showing a full-size gradient background with a button showcase.
 *
 * Uses DevicePreviewContainer to apply the provided dark/light theme and renders GradientBackground
 * (filling the available space) with ButtonShowcase as its content.
 *
 * @param isDarkTheme When `true`, the preview is rendered in dark mode; when `false`, in light mode.
 */
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