/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.indicator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ke.don.domain.table.AvatarBackground
import ke.don.resources.color

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FancyLoadingIndicator(
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = loading,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        FancyRefreshAnimation(
            modifier = modifier,
            isRefreshing = true,
        )
    }
}

/**
 * A visually engaging refresh indicator that can be used with pull-to-refresh implementations.
 * It consists of multiple `CircleWithRing` components that animate based on the pull progress
 * and refresh state.
 *
 * This animation is driven by three main states controlled by lambdas:
 * 1.  **Pull Progress:** `offsetProgress` controls the "opening" animation of the shapes as the user pulls down.
 * 2.  **Will Refresh:** `willRefresh` triggers a "bouncy" scale effect when the pull threshold is reached.
 * 3.  **Is Refreshing:** `isRefreshing` starts an infinite rotation animation while data is being loaded.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param isRefreshing A lambda that returns `true` when the refresh process is active, `false` otherwise.
 * @param state The state that keeps track of distance pulled
 *
 * Credit goes to https://www.sinasamaki.com/  for the component
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FancyRefreshAnimation(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    state: PullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {}
    ),
) {
    val colorA = AvatarBackground.PURPLE_LILAC.color()
    val colorB = AvatarBackground.GREEN_EMERALD.color()
    val colorC = AvatarBackground.YELLOW_BANANA.color()

    Row(
        modifier = modifier
            .padding(16.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        @Composable
        fun item(
            color: Color,
            alignment: Alignment
        ) = Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = alignment
        ) {
            CircleWithRing(
                modifier = Modifier.size(30.dp),
                isRefreshing = isRefreshing,
                willRefresh = isRefreshing || state.progress >= 1f,
                offsetProgress = if (isRefreshing) 1f else state.progress,
                shape = RoundedCornerShape(10.dp),
                color = color,
            )
        }

        item(colorA, Alignment.TopCenter)
        item(colorB, Alignment.BottomCenter)
        item(colorC, Alignment.Center)
        item(colorB, Alignment.BottomCenter)
        item(colorA, Alignment.TopCenter)
    }
}

@Composable
fun CircleWithRing(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    willRefresh: Boolean,
    offsetProgress: Float,
    shape: Shape = CircleShape,
    color: Color = Color.Yellow,
) {
    val scale by animateFloatAsState(
        targetValue = if (willRefresh) 1f else .8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
        ),
    )
    val rotateTransition = rememberInfiniteTransition()

    val rotation by when {
        isRefreshing -> rotateTransition.animateFloat(
            initialValue = 45f,
            targetValue = 180f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        )
        else -> remember { mutableStateOf(45f) }
    }

    Box(modifier = modifier.scale(scale)) {
        Box(
            modifier = Modifier
                .rotate(-rotation)
                .align(Alignment.Center)
                .scale(offsetProgress * 1.5f)
                .border(
                    width = 15.dp * (1f - offsetProgress),
                    shape = shape,
                    color = color,
                )
                .fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .rotate(rotation)
                .align(Alignment.Center)
                .scale(offsetProgress)
                .clip(shape)
                .background(color = color)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun rememberStaticPullToRefreshState(): PullToRefreshState {
    return remember {
        object : PullToRefreshState {
            override val distanceFraction: Float = 1f   // fully pulled
            override val isAnimating: Boolean = false

            override suspend fun animateToThreshold() {}
            override suspend fun animateToHidden() {}
            override suspend fun snapTo(targetValue: Float) {}
        }
    }
}