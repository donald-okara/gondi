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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

/**
 * A [Modifier] that adds a shimmer effect to the background of a Composable.
 *
 * This modifier creates a shimmering animation that can be used to indicate loading or an active state.
 * The shimmer effect is achieved by animating a horizontal gradient across the Composable.
 *
 * @param shape The [Shape] of the background to apply the shimmer to. Defaults to [RectangleShape].
 * @param shimmerColors A [List] of [Color]s to use for the shimmer gradient.
 *                      Defaults to a list of three colors derived from `MaterialTheme.colorScheme.surfaceVariant`
 *                      with varying alpha values to create a subtle shimmer.
 * @param durationMillis The duration of one shimmer animation cycle in milliseconds. Defaults to `1200`.
 * @param shimmerWidth The width of the shimmering gradient in pixels. Defaults to `500f`.
 * @return A [Modifier] that applies the shimmer background effect.
 */
/**
 * Apply a horizontal animated shimmer gradient as the background of a Modifier.
 *
 * @param shape The shape used for the background clipping. Defaults to a rectangle.
 * @param shimmerColors Colors used in the horizontal gradient from leading to trailing edge.
 * @param durationMillis Duration in milliseconds of a single shimmer cycle.
 * @param shimmerWidth Width in pixels of the moving shimmer highlight.
 * @return A Modifier with the animated shimmer background applied.
 */
@Composable
fun Modifier.shimmerBackground(
    shape: Shape = RectangleShape,
    shimmerColors: List<Color> = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    ),
    durationMillis: Int = 1200,
    shimmerWidth: Float = 500f,
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = shimmerWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerAnim",
    )

    val brush = Brush.horizontalGradient(
        colors = shimmerColors,
        startX = translateAnim,
        endX = translateAnim + shimmerWidth,
    )

    this.background(brush = brush, shape = shape)
}
