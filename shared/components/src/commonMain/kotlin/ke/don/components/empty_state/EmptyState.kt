/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.empty_state

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues

/**
 * Displays a centered empty-state screen with an optional icon, title, description, and action slot.
 *
 * The content is centered inside a full-size container with padding and uses Material theming for
 * typography and color. If `icon`, `description`, or `action` are `null`, their sections are omitted.
 *
 * @param modifier Modifier applied to the root container.
 * @param title The primary title text shown prominently.
 * @param description Optional secondary text shown beneath the title.
 * @param icon Optional leading icon shown above the title. Defaults to `Icons.Default.Info`.
 * @param action Optional composable slot rendered beneath the description for actions (e.g., a button).
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = Icons.Default.Info,
    emptyType: EmptyType = EmptyType.Empty,
    action: (@Composable RowScope.() -> Unit)? = null,
) {
    val baseColor = if (emptyType == EmptyType.Empty)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    val containerAlpha = 0.1f
    val borderAlpha = 0.5f

    // Optional soft entrance animation (feels great in empty screens)

    Surface(
        shape = Theme.shapes.medium,
        color = baseColor.copy(alpha = containerAlpha),
        contentColor = baseColor,
        tonalElevation = Theme.spacing.small,
        border = BorderStroke(
            width = Theme.spacing.tiny,
            color = baseColor.copy(alpha = borderAlpha)
        ),
        modifier = modifier
            .width(Theme.spacing.smallScreenSize),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = Theme.spacing.medium,
                    vertical = Theme.spacing.large
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.small)
        ) {

            // ICON
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = baseColor.copy(0.7f)
                )
            }

            // TITLE
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Theme.colorScheme.onSurface
            )

            // DESCRIPTION
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Theme.colorScheme.onSurfaceVariant
                )
            }

            // ACTION (optional)
            action?.let {
                Spacer(Modifier.height(Theme.spacing.medium))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.small, Alignment.End),
                    verticalArrangement = Arrangement.spacedBy(Theme.spacing.small)
                ){
                    it()
                }
            }
        }
    }
}



enum class EmptyType{
    Error, Empty
}