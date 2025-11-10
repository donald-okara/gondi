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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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

/**
 * Renders a responsive top app bar with an optional title, adaptive navigation icon, and actions.
 *
 * The title is displayed only when there is sufficient horizontal space. Navigation icon can be
 * the app logo, a back button (invokes `navigateBack`), or a burger/menu button (invokes `toggleDrawer`).
 * Action content is shown inline on wide layouts and arranged in a FlowRow beneath the app bar on narrow layouts.
 *
 * @param title Optional title text; displayed only when layout width is wide enough.
 * @param navigationIcon Controls the navigation area; supports `NavigationIcon.None`, `NavigationIcon.Back`, and `NavigationIcon.Burger`.
 * @param actions Composable slot for action content. Rendered inline in the app bar on wide screens or in a FlowRow on narrow screens.
 * @param isCompact When true, uses compact horizontal padding; otherwise uses expanded padding.
 * @param scrollBehavior Optional TopAppBarScrollBehavior to control app bar scrolling interactions.
 */
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (isCompact) Values.compactScreenPadding else Values.expandedScreenPadding),
    ) {
        TopAppBar(
            title = {
                title?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                    )
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
                actions()
            },
            scrollBehavior = scrollBehavior,
        )
        HorizontalDivider()
    }
}
