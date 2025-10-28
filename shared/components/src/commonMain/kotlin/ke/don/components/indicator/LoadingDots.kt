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

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Displays a horizontal row of circular dots that pulse their opacity in a staggered, wave-like pattern.
 *
 * @param modifier Modifier applied to the Row container.
 * @param dotCount Number of dots to display.
 * @param dotSize Diameter of each dot.
 * @param dotSpacing Horizontal space between adjacent dots.
 * @param color Fill color of the dots.
 * @param minAlpha Minimum alpha value for each dot.
 * @param maxAlpha Maximum alpha value for each dot.
 * @param pulseDuration Duration in milliseconds of one half-cycle (from `minAlpha` to `maxAlpha`).
 * @param delayBetweenDots Milliseconds to offset each dot's animation start to create the staggered wave.
 */
@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    dotCount: Int = 3,
    dotSize: Dp = 12.dp,
    dotSpacing: Dp = 8.dp,
    color: Color = Color.White,
    minAlpha: Float = 0.25f,
    maxAlpha: Float = 1f,
    pulseDuration: Int = 500, // one half-cycle in ms
    delayBetweenDots: Int = 150, // stagger delay in ms
) {
    val infinite = rememberInfiniteTransition()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(dotCount) { index ->
            // Each dot has its own animation with a start delay so they wave
            val alpha by infinite.animateFloat(
                initialValue = minAlpha,
                targetValue = maxAlpha,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = pulseDuration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * delayBetweenDots),
                ),
            )

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .alpha(alpha)
                    .background(color = color, shape = CircleShape),
            )
        }
    }
}