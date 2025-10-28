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
