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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.indicator.FancyRefreshAnimation
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.scaffold.RefreshLazyColumn
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.spacing
import ke.don.domain.datastore.Theme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

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
            repeat(steps) { step ->
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
    val pullState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { isRefreshing = true },
    )

    val cardRotation by animateFloatAsState(
        targetValue = when {
            isRefreshing || pullState.progress > 1f -> 5f
            pullState.progress > 0f -> 5 * pullState.progress
            else -> 0f
        }, label = "cardRotation"
    )

    val cardOffset by animateIntAsState(
        targetValue = when {
            isRefreshing -> 124
            pullState.progress in 0f..1f -> (124 * pullState.progress).roundToInt()
            pullState.progress > 1f -> (124 + ((pullState.progress - 1f) * .1f) * 100).roundToInt()
            else -> 0
        }, label = "cardOffset"
    )

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
            listOffSet = cardOffset,
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
                        .graphicsLayer {
                            rotationZ = cardRotation * if (index % 2 == 0) 1 else -1
                            translationY = (cardOffset * ((5f - (index + 1)) / 5f)).dp
                                .roundToPx()
                                .toFloat()
                        }
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


