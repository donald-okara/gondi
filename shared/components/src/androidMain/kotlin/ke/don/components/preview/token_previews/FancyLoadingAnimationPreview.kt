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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.indicator.FancyRefreshAnimation
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.domain.datastore.Theme
import kotlinx.coroutines.delay

// @DevicePreviews
// @Composable
// fun FilterChipPreview(
//    @PreviewParameter(ThemeProvider::class) theme: Theme,
// ){
//    DevicePreviewContainer(theme) {
//        FancyRefreshAnimation(
//            isRefreshing = { true },
//            willRefresh = { true },
//            offsetProgress = { 0f },
//        )
//    }
//
// }

@DevicePreviews
@Composable
fun FancyRefreshAnimationInteractivePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    // Simulate pull progression when refreshing
    if (isRefreshing) {
        LaunchedEffect(Unit) {
            // Animate progress up
            val duration = 400L
            val steps = 20
            repeat(steps) { step ->
                progress = (step + 1) / steps.toFloat()
                delay(duration / steps)
            }

            // Hold for effect
            delay(1200)

            // Reset
            isRefreshing = false
            progress = 0f
        }
    }

    DevicePreviewContainer(theme) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FancyRefreshAnimation(
                isRefreshing = { isRefreshing },
                willRefresh = { progress >= 1f },
                offsetProgress = { progress },
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { if (!isRefreshing) isRefreshing = true },
            ) {
                Text(if (isRefreshing) "Refreshing…" else "Trigger Refresh")
            }
        }
    }
}

@DevicePreviews
@Composable
fun FancyLoadingAnimationInteractivePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    var isLoading by remember { mutableStateOf(false) }

    // Simulate pull progression when refreshing
    if (isLoading) {
        LaunchedEffect(Unit) {
            // Animate progress up
            val duration = 400L
            val steps = 20
            repeat(steps) { step ->
                delay(duration / steps)
            }

            // Hold for effect
            delay(1200)

            // Reset
            isLoading = false
        }
    }

    DevicePreviewContainer(theme) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FancyLoadingIndicator(
                loading = isLoading,
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { if (!isLoading) isLoading = true },
            ) {
                Text(if (isLoading) "Refreshing…" else "Trigger Refresh")
            }
        }
    }
}
