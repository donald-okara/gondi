/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SampleScreen(name: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello, $name 👋",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = "This is a previewable screen with a name parameter.",
            style = MaterialTheme.typography.bodyLarge,
        )

        Button(
            onClick = { /* no-op in preview */ },
            shape = MaterialTheme.shapes.large, // 👈 platform-aware (round on mobile, sharp on desktop)
        ) {
            Text("Continue")
        }
    }
}
