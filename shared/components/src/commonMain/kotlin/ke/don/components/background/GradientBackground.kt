/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.background

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Renders a Box with a vertical background gradient and a continuously pulsing accent glow.
 *
 * The accent color fades vertically into the background color while its alpha pulses to create a subtle glow.
 *
 * @param modifier Modifier applied to the Box.
 * @param accentColor The color used at the start of the gradient; its alpha pulses to produce the glow.
 * @param backgroundColor The color used at the end of the gradient.
 * @param content Slot for the composable content placed centered inside the Box.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {
    // 🔄 Infinite glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowAlpha",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = glowAlpha),
                        backgroundColor,
                    ),
                    startY = Float.POSITIVE_INFINITY, // bottom
                    endY = 0f, // top
                ),
            ),
    ) {
        content()
    }
}
