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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a centered showcase of button variations for each entry in `ComponentType.entries`.
 *
 * For each entry this composable emits a row with two equal-width `ButtonToken` examples:
 * an enabled default button and a disabled loading button; both display the entry's name
 * and use no-op `onClick` handlers.
 */
@Composable
fun ButtonShowcase() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ComponentType.entries.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp), // spacing between buttons
            ) {
                ButtonToken(
                    onClick = {},
                    buttonType = it,
                    modifier = Modifier.weight(1f), // take half width
                ) {
                    Text(it.name)
                }
                ButtonToken(
                    onClick = {},
                    buttonType = it,
                    enabled = false,
                    loading = true,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(it.name)
                }
            }
        }
    }
}