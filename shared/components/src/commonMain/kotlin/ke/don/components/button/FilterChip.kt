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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun FilterDropdownChip(
    icon: ImageVector,
    enabled: Boolean = true,
    label: String,
    selected: Boolean,
    selectedComponentType: ComponentType = ComponentType.Inverse,
    showDefault: Boolean = true,
    options: List<Pair<String, String>>, // (value, display)
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    var buttonSize by remember { mutableStateOf(IntSize.Zero) }

    Box {
        IconButtonToken(
            icon = icon,
            enabled = enabled,
            buttonType = if (selected) selectedComponentType else ComponentType.Neutral,
            onClick = { expanded = true },
            text = label,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                buttonSize = coordinates.size
            },
        )

        ThemedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            showDefault = showDefault,
            items = options.map { it.second },
            onClear = {
                onClear()
                expanded = false
            },
            onItemClick = {
                onSelect(it)
                expanded = false
            },
            modifier = Modifier
                .width(with(LocalDensity.current) { buttonSize.width.toDp() }), // match button width
        )
    }
}

@Composable
fun ThemedDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<String>,
    showDefault: Boolean = true,
    onItemClick: (String) -> Unit,
    onClear: () -> Unit = {},
) {
    val contentColors = MenuItemColors(
        textColor = MaterialTheme.colorScheme.onSurface,
        leadingIconColor = MaterialTheme.colorScheme.onSurface,
        trailingIconColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(0.38f),
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.38f),
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.38f),
    )
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        if (showDefault) {
            DropdownMenuItem(
                text = { Text("All", style = MaterialTheme.typography.labelMedium) },
                colors = contentColors,
                onClick = onClear,
            )
        }
        items.forEach { item ->
            DropdownMenuItem(
                colors = contentColors,
                text = {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.labelMedium,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                onClick = { onItemClick(item) },
            )
        }
    }
}
