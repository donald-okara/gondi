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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.design.theme.AppTheme
import ke.don.domain.datastore.Theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
    theme: Theme,
    content: @Composable () -> Unit,
) {
    AppTheme(
        theme = theme,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            content.invoke()
        }
    }
}


@Composable
fun DeviceFramePreview(
    frame: DrawableResource,
    content: @Composable (() -> Unit)
) {
    Box(
        modifier = Modifier
            .aspectRatio(366f / 750f)
    ) {
        // UI clipped to screen area
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(16.dp) // adjust to screen cutout
        ) {
            content()
        }

        // Frame overlay on top
        Image(
            painter = painterResource(frame),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}
