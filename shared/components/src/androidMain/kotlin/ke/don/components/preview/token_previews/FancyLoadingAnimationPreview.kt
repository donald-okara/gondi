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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.indicator.FancyRefreshAnimation
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.scaffold.RefreshLazyColumn
import ke.don.components.scaffold.cardCrunchEffects
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.spacing
import ke.don.domain.datastore.Theme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@DevicePreviews
@Composable
fun FancyRefreshAnimationInteractivePreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    // Simulate pull progression when refreshing
    if (isRefreshing) {
        LaunchedEffect(Unit) {
            // Animate progress up
            val duration = 400L
            val steps = 20
            repeat(steps) {
                delay(duration / steps)
            }

            // Hold for effect
            delay(1200)

            // Reset
            isRefreshing = false
        }
    }

    DevicePreviewContainer(theme) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FancyRefreshAnimation(
                isRefreshing =  isRefreshing ,
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
            repeat(steps) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FancyLoadingIndicator(
                loading = isLoading,
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { if (!isLoading) isLoading = true },
            ) {
                Text(if (isLoading) "Loading…" else "Trigger Loading")
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@DevicePreviews
@Composable
fun RefreshLazyColumnPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    // Simulated refreshing state
    var isRefreshing by remember { mutableStateOf(false) }

    // Simple PullToRefreshState with no-op animation
    val pullState = rememberPullToRefreshState()

    // Toggle refresh after 2 seconds (for demo)
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }

    DevicePreviewContainer(theme){
        RefreshLazyColumn(
            isRefreshing = isRefreshing,
            onRefresh = { isRefreshing = true },
            pullRefreshState = pullState,
            horizontalPadding = PaddingOption.Custom(MaterialTheme.spacing.small),
            verticalPadding = PaddingOption.Custom(MaterialTheme.spacing.medium),
        ) {
            items(10) { index ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .cardCrunchEffects(
                            index = index,
                            isRefreshing = isRefreshing,
                            pullProgress = pullState.distanceFraction,
                        )
                        .padding(8.dp),
                ){
                    Text(
                        "Item #$index",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


