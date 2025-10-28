/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.design.theme.AppTheme

/**
 * Provides a themed full-screen container for previewing composables.
 *
 * Wraps the given content in the app's theme using the specified dark-mode setting and places it inside a Surface that fills the available space.
 *
 * @param isDarkTheme If `true`, applies the dark theme; otherwise applies the light theme.
 * @param content The composable content to render inside the themed full-size Surface.
 */
@Composable
fun DevicePreviewContainer(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(
        darkTheme = isDarkTheme,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            content.invoke()
        }
    }
}