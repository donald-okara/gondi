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

    @Composable
    fun textStyle(): TextStyle = when (this) {
        Small -> MaterialTheme.typography.labelSmall
        Medium -> MaterialTheme.typography.bodyMedium
    }
}
