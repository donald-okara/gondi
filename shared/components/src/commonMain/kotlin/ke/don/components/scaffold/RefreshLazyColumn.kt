/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.scaffold

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.FancyRefreshAnimation
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import kotlin.math.roundToInt

/**
 * A `LazyColumn` with integrated pull-to-refresh functionality.
 *
 * This composable wraps a `LazyColumn` within a `Box` that is modified with `pullRefresh`
 * to provide a swipe-to-refresh user experience. It displays a custom refresh indicator
 * (`RefreshHeader`) at the top, which becomes visible and animates as the user pulls down.
 *
 * Most parameters are standard `LazyColumn` parameters to allow for full customization of the
 * list's behavior and appearance.
 *
 * @param modifier The modifier to be applied to the outer `Box` container.
 * @param isRefreshing A boolean that controls the state of the refresh indicator. Set to `true`
 *   to show the refreshing state, and `false` to hide it.
 * @param onRefresh A lambda that is invoked when the user pulls down to trigger a refresh.
 *   This is where you should place your data-loading logic.
 * @param pullRefreshState The state object that manages the pull-to-refresh gesture. It's
 *   typically created with `rememberPullRefreshState`.
 * @param listOffSet The vertical offset applied to the `LazyColumn` to make space for the
 *   refresh indicator during the pull and refresh actions.
 * @param lazyListState The state object to be used for the `LazyColumn`, allowing observation
 *   and control of scroll position.
 * @param verticalPadding The vertical padding to be applied to the content of the `LazyColumn`.
 * @param horizontalPadding The horizontal padding to be applied to the content of the `LazyColumn`.
 * @param contentAlignment The alignment of the `LazyColumn` and `RefreshHeader` within the `Box`.
 *   Defaults to `Alignment.TopCenter`.
 * @param reverseLayout `true` to show items in reverse order, from bottom to top.
 * @param verticalArrangement The vertical arrangement of the `LazyColumn`'s children.
 */
@Composable
fun RefreshLazyColumn(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    listOffSet: Int = rememberOffset(
        isRefreshing = isRefreshing,
        pullProgress = pullRefreshState.distanceFraction,
    ),
    lazyListState: LazyListState = rememberLazyListState(),
    verticalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.medium),
    horizontalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.small),
    contentAlignment: Alignment = Alignment.TopCenter,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyListScope.() -> Unit,
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .fillMaxSize()
            .pullToRefresh(state = pullRefreshState, isRefreshing = isRefreshing, onRefresh = onRefresh),
    ) {
        LazyColumn(
            modifier = modifier.offset(
                x = 0.dp,
                y = listOffSet.dp,
            ),
            state = lazyListState,
            contentPadding = spacingPaddingValues(
                vertical = verticalPadding,
                horizontal = horizontalPadding,
            ),
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            overscrollEffect = overscrollEffect,
        ) {
            content()
        }

        RefreshHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = pullRefreshState,
        )
    }
}

/**
 * A composable that displays the header for the pull-to-refresh action.
 *
 * It manages its vertical position based on the pull progress and refresh state,
 * creating a smooth animation as the user pulls down. The header is initially
 * positioned off-screen and slides into view.
 *
 * The offset is calculated based on the following logic:
 * - When refreshing, it's fully visible at `150.dp`.
 * - During a pull gesture (progress from 0 to 1), it moves proportionally with the pull.
 * - If pulled beyond the threshold (progress > 1), it moves slightly further to give a "stretch" effect.
 *
 * @param modifier The modifier to be applied to the `FancyRefreshAnimation`.
 * @param isRefreshing A boolean indicating if the refresh operation is currently in progress.
 * @param state The [PullToRefreshState] that controls and represents the pull-to-refresh state.
 */
@Composable
private fun RefreshHeader(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    state: PullToRefreshState,
) {
    val animatedOffset by animateDpAsState(
        targetValue = when {
            isRefreshing -> 150.dp
            state.distanceFraction in 0f..1f -> (state.distanceFraction * 150).dp
            state.distanceFraction > 1f -> (150 + (((state.distanceFraction - 1f) * .1f) * 150)).dp
            else -> 0.dp
        },
        label = "animatedOffset",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .offset(y = (-150).dp)
            .offset { IntOffset(0, animatedOffset.roundToPx()) },
    ) {
        FancyRefreshAnimation(
            modifier = modifier,
            isRefreshing = isRefreshing,
            state = state,
        )
    }
}

/**
 * A `Modifier` that applies a visual "fanning out" effect to a list item during a pull-to-refresh gesture.
 *
 * This modifier uses `graphicsLayer` to apply rotation and vertical translation to a composable.
 * The effects are synchronized with the pull progress, creating an animation where items appear
 * to spread out like a deck of cards as the user pulls down.
 *
 * - The rotation (`rotationZ`) is determined by `rememberCardRotation` and applied differently to
 *   even and odd-indexed items to create a fanning effect.
 * - The vertical translation (`translationY`) is calculated based on `rememberOffset` and the item's
 *   index, causing items further down the list to be pushed down more, enhancing the spread effect.
 *
 * @param index The index of the item in the list. This is used to alternate the rotation direction
 *   and stagger the vertical offset.
 * @param isRefreshing A boolean indicating if the refresh operation is currently in progress.
 * @param pullProgress A float value representing the progress of the pull gesture, typically from 0.0 to 1.0.
 * @return A `Modifier` with the applied `graphicsLayer` transformations.
 */
@Composable
fun Modifier.cardCrunchEffects(
    index: Int,
    isRefreshing: Boolean,
    pullProgress: Float,
): Modifier {
    val rotation = rememberCardRotation(isRefreshing, pullProgress)
    val offset = rememberOffset(isRefreshing, pullProgress)

    return this.graphicsLayer {
        rotationZ = rotation * if (index % 2 == 0) 1 else -1
        translationY = (offset * ((5f - (index + 1)) / 5f))
            .dp.roundToPx()
            .toFloat()
    }
}

/**
 * Remembers and animates the rotation of a card-like element in a pull-to-refresh animation.
 *
 * This composable function calculates the target rotation angle based on the pull progress
 * and whether the refresh is currently active. It then animates the rotation to this
 * target value, providing a smooth visual effect as the user pulls down. The rotation
 * increases with the pull progress up to a maximum value, which is then maintained during
 * the refresh.
 *
 * @param isRefreshing A boolean indicating if the refresh operation is currently in progress.
 * @param pullProgress A float value from 0.0 to 1.0 (and beyond) representing the progress
 *                     of the pull gesture. 1.0 means the pull threshold has been reached.
 * @return The current, animated rotation value in degrees.
 */
@Composable
fun rememberCardRotation(
    isRefreshing: Boolean,
    pullProgress: Float,
): Float {
    val targetRotation = remember(isRefreshing, pullProgress) {
        computeCardRotation(isRefreshing, pullProgress)
    }

    return animateFloatAsState(
        targetValue = targetRotation,
        label = "cardRotation",
    ).value
}

/**
 * Computes the rotation angle for a card-like element based on the refresh state and pull progress.
 *
 * The rotation provides a subtle visual feedback cue during the pull-to-refresh gesture.
 *
 * - When not pulling (`progress` is 0 or less), the rotation is 0 degrees.
 * - As the user pulls down (`progress` from 0 to 1), the card rotates proportionally up to a maximum of 5 degrees.
 * - If the user over-pulls (`progress` > 1) or when the content is actively refreshing (`isRefreshing` is true),
 *   the rotation is capped at 5 degrees.
 *
 * @param isRefreshing `true` if the content is currently refreshing, `false` otherwise.
 * @param progress The current pull progress, typically a value between 0.0 and 1.0, but can exceed 1.0 if over-pulled.
 * @return The calculated rotation angle in degrees.
 */
private fun computeCardRotation(
    isRefreshing: Boolean,
    progress: Float,
): Float = when {
    isRefreshing || progress > 1f -> 5f
    progress > 0f -> 5f * progress
    else -> 0f
}

/**
 * A composable function that calculates and animates the vertical offset for a pull-to-refresh indicator.
 * This is designed to be used within a custom pull-to-refresh implementation.
 *
 * The offset is calculated based on the refresh state and the pull progress:
 * - When refreshing, the offset is a fixed value (124).
 * - During a pull gesture (progress from 0.0 to 1.0), the offset interpolates linearly up to 124.
 * - When over-pulling (progress > 1.0), the offset increases slowly to create a resistance effect.
 * - Otherwise, the offset is 0.
 *
 * The calculated offset is then animated using [animateIntAsState] to ensure smooth transitions.
 *
 * @param isRefreshing A boolean indicating if the content is currently being refreshed.
 * @param pullProgress A float representing the current pull progress, typically from a [PullToRefreshState].
 * @return An animated integer value representing the calculated vertical offset.
 */
@Composable
fun rememberOffset(
    isRefreshing: Boolean,
    pullProgress: Float,
): Int {
    val targetOffset = remember(isRefreshing, pullProgress) {
        computeOffset(isRefreshing, pullProgress)
    }

    return animateIntAsState(
        targetValue = targetOffset,
        label = "cardOffset",
    ).value
}

/**
 * Calculates the vertical offset of the refresh indicator card based on the pull progress
 * and refreshing state.
 *
 * This function determines the Y-position of the card within the refresh header.
 *
 * - When actively refreshing, the card is positioned at a fixed offset (124 pixels).
 * - During a pull gesture (progress from 0.0 to 1.0), the offset is proportional to the pull progress.
 * - If the user over-pulls (progress > 1.0), the offset increases slightly beyond the maximum
 *   to create a "stretching" effect.
 * - Otherwise, the offset is 0.
 *
 * @param isRefreshing `true` if the content is currently being refreshed, `false` otherwise.
 * @param progress The current pull progress, typically a value between 0.0 (not pulled) and 1.0 (fully pulled).
 *                 Can exceed 1.0 if over-pulled.
 * @return The calculated vertical offset in pixels as an [Int].
 */
private fun computeOffset(
    isRefreshing: Boolean,
    progress: Float,
): Int = when {
    isRefreshing ->
        124

    progress in 0f..1f ->
        (124 * progress).roundToInt()

    progress > 1f ->
        124 + (((progress - 1f) * 0.1f) * 100).roundToInt()

    else ->
        0
}
