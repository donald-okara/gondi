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

/**
 * Renders a configurable Material Button whose appearance is driven by the provided ComponentType and that delegates inner content rendering to ButtonContentSwitcher.
 *
 * @param modifier Layout modifier applied to the button.
 * @param onClick Lambda invoked when the button is clicked.
 * @param buttonType Determines the button's visual variant and color animation.
 * @param enabled Whether the button is interactive.
 * @param loading When `true`, ButtonContentSwitcher will render the loading state instead of the provided content.
 * @param shape The shape used to clip the button.
 * @param elevation Elevation configuration for the button.
 * @param border Optional border stroke; typically provided when `buttonType` is an outlined variant.
 * @param contentPadding Padding inside the button around its content.
 * @param interactionSource Optional interaction source for gesture and interaction tracking.
 * @param content Composable content displayed inside the button when not loading.
 */
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
    paddingValues: PaddingValues = ButtonDefaults.ContentPadding,
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
        contentPadding = paddingValues,
        interactionSource = interactionSource,
    ) {
        ButtonContentSwitcher(
            loading = loading,
            buttonType = buttonType,
            content = content,
        )
    }
}
