/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.button.FilterDropdownChip
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews

/**
 * Displays a preview of the FilterDropdownChip demonstrating selection, label display, and clearing behavior.
 *
 * Renders the chip inside a device preview container with a Surface and Column layout; the chip shows "Select Type" when nothing is selected
 * and offers the options "Bug", "Feature", and "Task".
 *
 * @param isDarkTheme Whether the preview should render using a dark theme.
 */
@DevicePreviews
@Composable
fun FilterDropdownChipPreview(
    @PreviewParameter(DarkModeProvider::class) isDarkTheme: Boolean,
) {
    DevicePreviewContainer(isDarkTheme) {
        var selected by remember { mutableStateOf<String?>(null) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                FilterDropdownChip(
                    icon = Icons.AutoMirrored.Filled.Sort,
                    label = selected ?: "Select Type",
                    selected = selected != null,
                    options = listOf(
                        "bug" to "Bug",
                        "feature" to "Feature",
                        "task" to "Task",
                    ),
                    onSelect = { value -> selected = value },
                    onClear = { selected = null },
                )
            }
        }
    }
}
