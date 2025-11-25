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

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

@Composable
fun GlowingSelectableSurface(
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    glowingColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val isActivated = enabled && (selected || hovered)

    // Animate a single float to drive all animations, promoting consistency.
    val activationAnimation by animateFloatAsState(
        targetValue = if (isActivated) 1f else 0f,
        label = "activationAnimation",
    )

    // Animate a pulsing glow effect when selected.
    val infiniteTransition = rememberInfiniteTransition(label = "infinite glow")
    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAnimation",
    )
    val selectionGlow = if (selected) pulseAnimation else 1f

    val ambientColor = glowingColor.copy(alpha = lerp(0.3f, 0.7f, activationAnimation))
    val spotColor = glowingColor.copy(alpha = lerp(0.4f, 1f, activationAnimation) * selectionGlow)
    val borderThickness = Theme.spacing.tiny * activationAnimation
    val borderColor = glowingColor.copy(alpha = lerp(0.6f, 1f, activationAnimation) * selectionGlow)
    val surfaceTint = lerp(0.0f, 0.3f, activationAnimation) * selectionGlow

    val baseSurfaceColor = if (enabled) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.38f)
    }
    val blendedSurfaceColor = baseSurfaceColor.copy(
        red = lerp(baseSurfaceColor.red, glowingColor.red, surfaceTint),
        green = lerp(baseSurfaceColor.green, glowingColor.green, surfaceTint),
        blue = lerp(baseSurfaceColor.blue, glowingColor.blue, surfaceTint),
    )

    val elevation = if (enabled) Theme.spacing.small else 0.dp
    val border = BorderStroke(
        width = borderThickness,
        color = borderColor,
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.shadow(elevation, shape, ambientColor = ambientColor, spotColor = spotColor),
        shape = shape,
        color = blendedSurfaceColor,
        contentColor = contentColorFor(blendedSurfaceColor),
        tonalElevation = elevation,
        border = border,
        interactionSource = interaction,
    ) {
        content()
    }
}

// Linear interpolation for Float
private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

// Linear interpolation for Dp
private fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
    return Dp(lerp(start.value, stop.value, fraction))
}
