/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.text_field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldToken(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    placeholder: String? = null,
    isError: Boolean = false,
    comment: String? = null,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showLength: Boolean = false,
    nameLength: Int = text.length,
    maxLength: Int = 0,
    onClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
    isRequired: Boolean = false,
    minLines: Int = 1,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val counterColor by remember(isError, nameLength, maxLength) {
        derivedStateOf {
            when {
                isError -> colorScheme.error
                maxLength > 0 && nameLength >= maxLength - 5 -> colorScheme.tertiary
                else -> colorScheme.onSurfaceVariant
            }
        }
    }

    val iconColor by remember(enabled, isError) {
        derivedStateOf {
            if (enabled && !isError) {
                colorScheme.primary
            } else {
                colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder?.let { { Text(it) } },
            label = {
                LabelWithRequiredMarker(label = label, isRequired = isRequired)
            },
            shape = RoundedCornerShape(12.dp),
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(imageVector = icon, contentDescription = label)
                }
            },
            trailingIcon = if (onClick != null && trailingIcon != null) {
                {
                    IconButton(onClick = { if (enabled && !isError) onClick() }) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = label,
                            tint = iconColor,
                        )
                    }
                }
            } else {
                null
            },
            supportingText = {
                when {
                    isError && !errorMessage.isNullOrEmpty() -> {
                        Text(
                            text = errorMessage,
                            style = typography.labelSmall,
                            color = colorScheme.error,
                        )
                    }

                    !comment.isNullOrEmpty() -> {
                        Text(
                            text = comment,
                            style = typography.labelSmall,
                            color = colorScheme.secondary,
                        )
                    }
                }
            },
        )

        if (showLength && maxLength > 0) {
            Text(
                text = "$nameLength / $maxLength",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = typography.labelSmall,
                color = counterColor,
            )
        }
    }
}

@Composable
private fun LabelWithRequiredMarker(
    label: String,
    isRequired: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label)
        if (isRequired) {
            Text(
                text = " *",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
