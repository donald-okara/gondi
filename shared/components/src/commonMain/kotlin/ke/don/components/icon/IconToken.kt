/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.icon

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
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.button.animatedIconColors
import ke.don.resources.Values

/**
 * Renders an icon that can be optionally interactive and animates between icons using a crossfade.
 *
 * If `onClick` is provided the icon is wrapped in a ButtonToken using `buttonType`, `contentPaddingValues`
 * and `enabled`; otherwise the icon is placed in a Box with the given `modifier`. When `content` is supplied
 * it replaces the default animated icon rendering.
 *
 * @param onClick Optional click handler; when non-null the icon is rendered inside a ButtonToken.
 * @param modifier Modifier applied to the outer container when not using a ButtonToken.
 * @param imageVector The vector graphic to display as the icon.
 * @param buttonType Visual style to apply when the icon is rendered as a button.
 * @param contentDescription Accessibility description for the icon.
 * @param contentPaddingValues Padding to apply when the icon is placed inside a ButtonToken.
 * @param enabled Whether the surrounding button is enabled when `onClick` is provided.
 * @param size Size of the displayed icon.
 * @param colors Colors used for the icon content (tint).
 * @param content Optional custom composable content that replaces the default animated icon.
 */
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
