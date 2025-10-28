/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.design_system.components.icon

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.design_system.assets.Values
import ke.don.design_system.components.button.ButtonToken
import ke.don.design_system.components.button.ComponentType
import ke.don.design_system.components.button.animatedIconColors

@Composable
fun IconToken(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    buttonType: ComponentType = ComponentType.Neutral,
    contentDescription: String? = null,
    contentPaddingValues: PaddingValues = Values.buttonPaddingValues,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    colors: IconButtonColors = buttonType.animatedIconColors(),
    content: (@Composable () -> Unit)? = null,
) {
    val animatedIcon: @Composable () -> Unit = {
        Crossfade(
            targetState = imageVector,
            label = "icon crossfade",
        ) { target ->
            Icon(
                imageVector = target,
                contentDescription = contentDescription,
                tint = colors.contentColor,
                modifier = Modifier.size(size),
            )
        }
    }

    if (onClick != null) {
        ButtonToken(
            onClick = onClick,
            enabled = enabled,
            buttonType = buttonType,
            contentPadding = contentPaddingValues,
        ) {
            content?.invoke() ?: animatedIcon()
        }
    } else {
        Box(modifier = modifier) {
            content?.invoke() ?: animatedIcon()
        }
    }
}
