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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.LoadingDots

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
                color = buttonType.animatedButtonColors().contentColor,
                dotSize = contentHeight ?: 20.dp,
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    }
}
