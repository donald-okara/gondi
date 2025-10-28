/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.runtime.Composable
import ke.don.components.icon.IconToken
import ke.don.resources.isCompact
import kotlin.invoke

/**
 * Displays a refresh control that animates between a compact icon and a full labeled button.
 *
 * When `isCompact` is true an icon-only button is shown; when false a labeled button with optional
 * loading state is shown. Transitions between states use a horizontal slide with cross-fade.
 *
 * @param isCompact If true shows the compact icon variant; if false shows the expanded labeled variant.
 * @param enabled Whether the button is interactive.
 * @param loading When true and the expanded variant is shown, the button displays a loading state.
 * @param onClick Callback invoked when the button is pressed.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RefreshButton(
    isCompact: Boolean = isCompact(),
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = isCompact,
        transitionSpec = {
            // Horizontal slide + fade transition
            (slideInHorizontally { it } + fadeIn()) togetherWith
                (slideOutHorizontally { -it } + fadeOut())
        },
        label = "refresh-button",
    ) { compact ->
        if (compact) {
            IconToken(
                onClick = onClick,
                enabled = enabled,
                imageVector = Icons.Default.Repeat,
                contentDescription = "Refresh",
                buttonType = ComponentType.Neutral,
            )
        } else {
            IconButtonToken(
                buttonType = ComponentType.Outlined,
                onClick = onClick,
                enabled = enabled,
                loading = loading,
                icon = Icons.Default.Repeat,
                text = "Refresh",
            )
        }
    }
}