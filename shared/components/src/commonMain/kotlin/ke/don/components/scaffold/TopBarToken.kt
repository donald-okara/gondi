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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.resources.Resources
import ke.don.resources.Values
import ke.don.resources.isCompact
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopBarToken(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: NavigationIcon = NavigationIcon.None,
    actions: @Composable RowScope.() -> Unit = {},
    isCompact: Boolean = isCompact(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidth = maxWidth

        val wideEnough = maxWidth > 360.dp
        val useFlowRow = !wideEnough

        Column(
            modifier = modifier.padding(horizontal = if (isCompact) Values.compactScreenPadding else Values.expandedScreenPadding),
        ) {
            TopAppBar(
                title = {
                    if (wideEnough) {
                        title?.let {
                            Text(
                                text = it,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                },
                navigationIcon = {
                    when (navigationIcon) {
                        NavigationIcon.None -> {
                            Image(
                                painter = painterResource(Resources.Images.LOGO),
                                contentDescription = "App Icon",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        is NavigationIcon.Back -> IconButton(onClick = navigationIcon.navigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        is NavigationIcon.Burger -> IconButton(onClick = navigationIcon.toggleDrawer) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                },
                actions = {
                    if (!useFlowRow) {
                        actions()
                    }
                },
                scrollBehavior = scrollBehavior,
            )

            if (useFlowRow) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    actions()
                }
            }

            HorizontalDivider()
        }
    }
}
