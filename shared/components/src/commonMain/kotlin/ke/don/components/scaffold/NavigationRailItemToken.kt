/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.scaffold

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Renders a navigation rail item with a leading icon and an optional label.
 *
 * The item's background color animates between surface and surfaceVariant based on `selected`.
 *
 * @param selected Whether the item is in a selected state.
 * @param onClick Lambda invoked when the item is clicked.
 * @param icon The icon to display at the start of the item.
 * @param modifier Modifier applied to the item's surface.
 * @param enabled Whether the item is interactive.
 * @param label Text used for the item's content description and shown as the label when expanded.
 * @param expanded When `true`, the label is displayed and the item expands to fill available width; when `false`, only the icon is shown.
 */
@Composable
fun NavigationRailItemToken(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    expanded: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 250),
        label = "contentColor",
    )
    Surface(
        onClick = onClick,
        color = containerColor,
        shape = MaterialTheme.shapes.medium,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier
            .then(if (expanded) Modifier.fillMaxWidth() else Modifier),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
            )

            if (expanded) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}