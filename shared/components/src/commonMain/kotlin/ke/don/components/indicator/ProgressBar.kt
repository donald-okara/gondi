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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.design.theme.spacing

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    height: Dp = MaterialTheme.spacing.small,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progressAnimation",
    )

    val animatedProgressColor by animateColorAsState(
        targetValue = progressColor,
        label = "animatedColor",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp),
    ) {
        val barWidth = size.width
        val barHeight = height.toPx()
        val radius = barHeight / 2
        val progressWidth = barWidth * animatedProgress

        // Draw background bar
        drawRoundRect(
            color = backgroundColor,
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(radius, radius),
            topLeft = Offset(0f, (size.height - barHeight) / 2),
        )

        // Draw progress with rounded ends
        if (animatedProgress > 0) {
            drawRoundRect(
                color = animatedProgressColor,
                size = Size(progressWidth, barHeight),
                cornerRadius = CornerRadius(radius, radius),
                topLeft = Offset(0f, (size.height - barHeight) / 2),
            )
        }
    }
}
