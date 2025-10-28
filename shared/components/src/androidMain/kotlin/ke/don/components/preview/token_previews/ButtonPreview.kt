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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ke.don.components.button.ButtonShowcase
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews

class DarkModeProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

/**
 * Preview composable that renders ButtonShowcase in both light and dark theme variants.
 *
 * Renders ButtonShowcase inside DevicePreviewContainer using the provided `isDarkTheme` flag.
 *
 * @param isDarkTheme When `true`, the preview uses a dark theme; when `false`, it uses a light theme.
 */
@DevicePreviews
@Composable
fun ButtonPreview(
    @PreviewParameter(DarkModeProvider::class) isDarkTheme: Boolean,
) {
    DevicePreviewContainer(
        isDarkTheme,
    ) {
        ButtonShowcase()
    }
}