/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.guide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NoSim
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

@Composable
fun CodeOfConductSection(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = Theme.shapes.medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        tonalElevation = Theme.spacing.small,
        border = BorderStroke(
            width = Theme.spacing.tiny,
            color = Theme.colorScheme.primary.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Gavel,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(Theme.spacing.large),
            )

            Spacer(Modifier.height(Theme.spacing.large))

            Text(
                text = stringResource(Resources.Strings.Guide.CODE_OF_CONDUCT),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(Resources.Strings.Guide.CODE_OF_CONDUCT_DESCRIPTION),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(Theme.spacing.large))

            val minSize = 240.dp
            val spacing = MaterialTheme.spacing.medium

            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val cols = ((maxWidth - spacing * 2) / minSize)
                    .toInt()
                    .coerceIn(1, 3) // min 1, max 3 columns

                val itemWidth = (maxWidth - spacing * (cols - 1)) / cols

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.NoSim,
                        title = stringResource(Resources.Strings.Guide.NO_PEEKING),
                        description = stringResource(Resources.Strings.Guide.NO_PEEKING_DESCRIPTION),
                    )

                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.VisibilityOff,
                        title = stringResource(Resources.Strings.Guide.KEEP_IT_SECRET),
                        description = stringResource(Resources.Strings.Guide.KEEP_IT_SECRET_DESCRIPTION),
                    )

                    ConductItem(
                        modifier = Modifier.width(itemWidth),
                        icon = Icons.Default.SentimentSatisfied,
                        title = stringResource(Resources.Strings.Guide.PLAY_NICE),
                        description = stringResource(Resources.Strings.Guide.PLAY_NICE_DESCRIPTION),
                    )
                }
            }
        }
    }
}

@Composable
private fun ConductItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(0.9f),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(Theme.spacing.large),
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
