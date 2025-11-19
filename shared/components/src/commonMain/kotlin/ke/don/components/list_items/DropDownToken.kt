/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.list_items

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme

@Composable
fun DropDownToken(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    items: List<DropDownData>,
    onDismiss: () -> Unit,
) {
    if (!expanded) return

    val (destructive, normal) = remember(items) {
        items.partition { it.destructive }
    }

    DropdownMenu(
        modifier = modifier,
        expanded = true,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        onDismissRequest = onDismiss,
        containerColor = Theme.colorScheme.surface,
    ) {
        normal.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onDismiss()
                    item.onClick()
                },
                colors = MenuDefaults.itemColors().copy(
                    textColor = Theme.colorScheme.onSurface,
                    disabledTextColor = Theme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
                text = {
                    Text(
                        item.title,
                        color = Theme.colorScheme.onSurface,
                    )
                },
            )
        }

        if (destructive.isNotEmpty() && normal.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier.padding(4.dp),
            )
        }

        destructive.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onDismiss()
                    item.onClick()
                },
                text = {
                    Text(
                        item.title,
                        color = MaterialTheme.colorScheme.error,
                    )
                },
            )
        }
    }
}

data class DropDownData(
    val title: String,
    val onClick: () -> Unit,
    val destructive: Boolean = false,
)
