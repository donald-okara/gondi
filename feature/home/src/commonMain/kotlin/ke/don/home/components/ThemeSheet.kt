/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import ke.don.components.dialog.BottomSheetToken
import ke.don.design.theme.spacing
import ke.don.domain.datastore.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onThemeChange: (Theme) -> Unit,
    currentTheme: Theme,
) {
    BottomSheetToken(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                Crossfade(
                    targetState = currentTheme,
                    animationSpec = tween(400),
                    label = "theme_icon_animation",
                ) { theme ->
                    val icon = when (theme) {
                        Theme.Dark -> Icons.Default.DarkMode
                        Theme.Light -> Icons.Default.LightMode
                        Theme.System -> Icons.Default.Style
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Theme.entries.forEach { theme ->
                    val selected = theme == currentTheme
                    val label = when (theme) {
                        Theme.Light -> "Light"
                        Theme.Dark -> "Dark"
                        Theme.System -> "System default"
                    }

                    val bg by animateColorAsState(
                        targetValue = if (selected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        } else {
                            Color.Transparent
                        },
                        label = "theme_background",
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.02f else 1f,
                        label = "theme_scale",
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(bg)
                            .clickable { onThemeChange(theme) }
                            .padding(MaterialTheme.spacing.medium)
                            .graphicsLayer {
                                this.scaleX = scale
                                this.scaleY = scale
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        RadioButton(
                            selected = selected,
                            onClick = { onThemeChange(theme) },
                        )
                    }
                }
            }
        }
    }
}
