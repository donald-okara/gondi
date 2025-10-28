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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import ke.don.resources.Values

@Composable
fun ButtonToken(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonType: ComponentType,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = if (buttonType == ComponentType.Outlined) ButtonDefaults.outlinedButtonBorder() else null,
    contentPadding: PaddingValues = Values.buttonPaddingValues,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (RowScope.() -> Unit),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = buttonType.animatedButtonColors(),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        ButtonContentSwitcher(
            loading = loading,
            buttonType = buttonType,
            content = content,
        )
    }
}
