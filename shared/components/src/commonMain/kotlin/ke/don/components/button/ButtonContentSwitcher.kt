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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.LoadingDots
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

/**
 * Displays either a loading indicator or the provided button content and animates transitions between them.
 *
 * When `loading` is true, a LoadingDots indicator is shown; when false, `content` is rendered inside a horizontally
 * centered Row. Transitions use a short cross-fade animation. The loading indicator's dot size uses the measured
 * content height when available, otherwise falls back to 20.dp.
 *
 * @param loading Whether to show the loading indicator instead of the content.
 * @param buttonType The button's component type (accepted for API compatibility; not used by this switcher).
 * @param content Composable slot that provides the button's normal content when not loading.
 */
@Composable
fun ButtonContentSwitcher(
    loading: Boolean,
    buttonType: ComponentType,
    content: @Composable RowScope.() -> Unit,
) {
    var contentHeight by remember { mutableStateOf<Dp?>(null) }

    AnimatedContent(
        targetState = loading,
        transitionSpec = {
            fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
        },
        label = "ButtonContentAnimation",
    ) { isLoading ->
        if (isLoading) {
            LoadingDots(
                color = LocalContentColor.current,
                dotSize = contentHeight ?: 20.dp,
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.small)
            ) {
                content()
            }
        }
    }
}
