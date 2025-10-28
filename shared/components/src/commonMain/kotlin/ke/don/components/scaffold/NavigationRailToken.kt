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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A customizable navigation rail surface with optional header, content, and footer slots.
 *
 * The rail uses a medium-shaped Surface and arranges slot content in a vertically scrollable,
 * selectable column. When `expanded` is true the rail is fixed to 256.dp and dividers span the
 * full width; when false the rail wraps its content and dividers are 30.dp wide.
 *
 * @param modifier Modifier applied to the outer Surface.
 * @param containerColor Background color of the rail Surface.
 * @param contentColor Default content color used inside the rail.
 * @param header Optional composable invoked at the top of the rail; when present a divider and
 *   spacing are placed after it.
 * @param footer Optional composable invoked at the bottom of the rail; when present content is
 *   pushed toward the top and a divider is placed before the footer.
 * @param windowInsets Insets applied as padding to the rail's inner content.
 * @param content Optional main content composable placed between header and footer.
 * @param expanded When true the rail is 256.dp wide and dividers fill the width; when false the
 *   rail wraps its width and dividers are 30.dp wide.
 */
@Composable
fun NavigationRailToken(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    footer: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    expanded: Boolean = true,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .then(if (expanded) Modifier.width(256.dp) else Modifier.wrapContentWidth()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(windowInsets)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            header?.let {
                it()
                HorizontalDivider(
                    modifier = Modifier
                        .then(if (expanded) Modifier.fillMaxWidth() else Modifier.width(30.dp))
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(8.dp))
            }

            content?.invoke(this)

            footer?.let {
                Spacer(Modifier.weight(1f))
                HorizontalDivider(
                    modifier = Modifier
                        .then(if (expanded) Modifier.fillMaxWidth() else Modifier.width(30.dp))
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally),
                )
                it()
            }
        }
    }
}