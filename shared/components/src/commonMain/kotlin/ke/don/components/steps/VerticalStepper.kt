/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.components.icon.IconBox

fun <T> LazyListScope.verticalSteps(
    items: List<VerticalStep<T>>,
    content: @Composable (T) -> Unit,
) {
    items(
        items = items,
        key = { it.index },
    ) { step ->
        VerticalStepItem(
            step = step,
            content = content,
        )
    }
}

@Composable
fun <T> VerticalStepItem(
    step: VerticalStep<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    val tint = step.color ?: MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // 👈 critical
    ) {
        // Stepper column
        Column(
            modifier = Modifier
                .width(36.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Circle
            IconBox(
                icon = step.icon ?: Icons.Outlined.Circle,
                accentColor = tint,
                sizeInt = 30,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Bottom connector
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .width(2.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            )
        }

        Spacer(Modifier.width(16.dp))

        // Content
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            content(step.data)
        }
    }
}
