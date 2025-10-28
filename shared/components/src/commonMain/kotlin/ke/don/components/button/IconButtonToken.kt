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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Renders a button that displays an icon to the left of a text label, with configurable size, visual variant, and interaction state.
 *
 * @param modifier Modifier applied to the button container.
 * @param onClick Lambda invoked when the button is clicked.
 * @param buttonType Visual/semantic variant of the button.
 * @param icon The icon image to display; its content description is set to `text`.
 * @param text The label shown next to the icon.
 * @param loading When `true`, the button shows a loading state.
 * @param enabled When `false`, the button is not interactive.
 * @param buttonSize Controls the icon size and the text typography used.
 */
@Composable
fun IconButtonToken(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    buttonType: ComponentType = ComponentType.Primary,
    icon: ImageVector,
    text: String,
    loading: Boolean = false,
    enabled: Boolean = true,
    buttonSize: ButtonSize = ButtonSize.Small,
) {
    ButtonToken(
        buttonType = buttonType,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.width(buttonSize.iconSize),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = buttonSize.textStyle(),
        )
    }
}

enum class ButtonSize(val iconSize: Dp) {
    Small(16.dp),
    Medium(24.dp),
    ;

    /**
     * Provides the typography TextStyle that corresponds to this ButtonSize.
     *
     * @return The TextStyle for the size: `Small` maps to `MaterialTheme.typography.labelSmall`, `Medium` maps to `MaterialTheme.typography.bodyMedium`.
     */
    @Composable
    fun textStyle(): TextStyle = when (this) {
        Small -> MaterialTheme.typography.labelSmall
        Medium -> MaterialTheme.typography.bodyMedium
    }
}
