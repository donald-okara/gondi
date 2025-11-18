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

/**
 * A composable that displays the [FancyRefreshAnimation] when the `loading` state is true.
 * It uses [AnimatedVisibility] to provide a smooth fade-in/scale-in and fade-out/scale-out
 * transition. This is useful for showing a loading indicator in the center of the screen
 * or in place of content while data is being fetched, independent of a pull-to-refresh action.
 *
 * @param loading A boolean that determines whether the loading indicator is visible.
 * @param modifier The modifier to be applied to the underlying [FancyRefreshAnimation].
 */
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
@Composable
fun FancyRefreshAnimation(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    state: PullToRefreshState = rememberPullToRefreshState(),
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
                willRefresh = isRefreshing || state.distanceFraction >= 1f,
                offsetProgress = if (isRefreshing) 1f else state.distanceFraction,
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

/**
 * A composable that displays an animated shape, consisting of a central filled shape and an
 * outer ring. The animations are driven by the state of a pull-to-refresh action.
 *
 * The animation has three key stages:
 * 1.  **Pulling**: As the user pulls (`offsetProgress` increases), the central shape and outer ring
 *     scale up from zero. The ring's border width decreases, creating an "opening" effect.
 * 2.  **Threshold Reached**: When the pull distance is sufficient (`willRefresh` is true), the entire
 *     component scales up with a bouncy spring animation to indicate it's ready to refresh.
 * 3.  **Refreshing**: When `isRefreshing` is true, the central shape and outer ring start rotating
 *     in opposite directions in an infinite loop, providing a loading indicator.
 *
 * @param modifier The modifier to be applied to the component.
 * @param isRefreshing `true` if the content is currently refreshing, which triggers the infinite
 *   rotation animation.
 * @param willRefresh `true` if the pull-to-refresh threshold has been met, triggering the bouncy
 *   scale-up animation.
 * @param offsetProgress The progress of the pull action, typically a float from 0.0 to 1.0 (or more),
 *   which controls the scale and border width of the shapes.
 * @param shape The `Shape` to be used for both the central component and the outer ring.
 * @param color The `Color` to be applied to the shape and ring.
 */
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