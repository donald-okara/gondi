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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Renders a labeled OutlinedTextField with optional placeholder, icons, error/comment supporting text, and an optional character counter.
 *
 * Displays the label (with an optional required marker), binds to `text`/`onValueChange`, and conditionally shows a trailing action icon when `onClick` and `trailingIcon` are provided. When `isError` and `errorMessage` are present the error message is shown instead of `comment`. If `showLength` is true and `maxLength` > 0, a right-aligned counter of `nameLength / maxLength` is displayed. The trailing action is only invoked when the field is enabled and not in an error state.
 *
 * @param modifier Modifier applied to the root column.
 * @param label Visible label for the text field.
 * @param text Current text value.
 * @param onValueChange Callback invoked when the text changes.
 * @param enabled Whether the text field is enabled for interaction.
 * @param placeholder Optional placeholder text shown when the field is empty.
 * @param isError Whether the field is in an error state.
 * @param comment Optional helper or descriptive text shown when not displaying an error.
 * @param errorMessage Optional error text shown when `isError` is true.
 * @param readOnly If true, the field is not editable.
 * @param singleLine If true, the field limits input to a single line.
 * @param keyboardOptions Keyboard input configuration.
 * @param keyboardActions Keyboard action callbacks.
 * @param showLength Whether to show the character counter.
 * @param nameLength Current character count used by the counter (defaults to `text.length`).
 * @param maxLength Maximum character count used by the counter; ignored when <= 0.
 * @param onClick Optional callback for the trailing icon action.
 * @param trailingIcon Optional image vector displayed as the trailing icon (requires `onClick` to be actionable).
 * @param leadingIcon Optional image vector displayed as the leading icon.
 * @param isRequired Whether to append a required-field marker to the label.
 * @param minLines Minimum number of visible lines for the text field.
 */
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

    val counterColor by animateColorAsState(
        targetValue = when {
            isError -> colorScheme.error
            maxLength > 0 && nameLength >= maxLength - 5 -> colorScheme.tertiary
            else -> colorScheme.onSurfaceVariant
        },
        label = "Counter Color Animation",
    )

    val iconColor by animateColorAsState(
        targetValue = if (enabled && !isError) colorScheme.primary else colorScheme.onSurfaceVariant.copy(
            alpha = 0.6f
        ), label = "Icon Color Animation"
    )

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
                AnimatedVisibility(visible = isError && !errorMessage.isNullOrEmpty()) {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            style = typography.labelSmall,
                            color = colorScheme.error,
                        )
                    }
                }
                AnimatedVisibility(visible = !isError && !comment.isNullOrEmpty()) {
                    if (comment != null) {
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

/**
 * Render a label with an optional red asterisk to indicate a required field.
 *
 * @param label The label text to display.
 * @param isRequired If `true`, appends a red "*" using `bodySmall` typography to indicate the field is required.
 */
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
