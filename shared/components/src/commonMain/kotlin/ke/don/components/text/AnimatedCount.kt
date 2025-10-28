/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.text

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Displays an integer that animates from its current value to the provided target value.
 *
 * @param targetValue The destination integer value to animate toward.
 * @param modifier Modifier applied to the Text composable.
 * @param style TextStyle used to render the number.
 * @param fontWeight FontWeight applied to the text.
 * @param durationMillis Duration of the animation in milliseconds.
 */
@Composable
fun AnimatedCount(
    targetValue: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    durationMillis: Int = 800,
) {
    val animatedValue by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "countAnim",
    )

    Text(
        text = animatedValue.toString(),
        style = style,
        fontWeight = fontWeight,
        modifier = modifier,
    )
}