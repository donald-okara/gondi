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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.profile.AdaptiveLogo
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.resources.isCompact

/**
 * Renders a responsive top app bar with an optional title, adaptive navigation icon, and actions.
 *
 * The title is displayed only when there is sufficient horizontal space. Navigation icon can be
 * the app logo, a back button (invokes `navigateBack`), or a burger/menu button (invokes `toggleDrawer`).
 * Action content is shown inline on wide layouts and arranged in a FlowRow beneath the app bar on narrow layouts.
 *
 * @param title Optional title text; displayed only when layout width is wide enough.
 * @param navigationIcon Controls the navigation area; supports `NavigationIcon.None`, `NavigationIcon.Back`, and `NavigationIcon.Custom`.
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
            .fillMaxWidth(),
    ) {
        TopAppBar(
            title = {
                title?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(Theme.spacing.small),
                    )
                }
            },
            navigationIcon = {
                when (navigationIcon) {
                    NavigationIcon.None -> {
                        AdaptiveLogo(
                            modifier = Modifier
                                .padding(Theme.spacing.small)
                                .size(24.dp),
                        )
                    }
                    is NavigationIcon.Back ->
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(Theme.spacing.small)
                                .clickable {
                                    navigationIcon.navigateBack()
                                },
                        )

                    is NavigationIcon.Custom -> navigationIcon.content()
                }
            },
            actions = {
                Row(
                    modifier = Modifier
                        .padding(Theme.spacing.small),
                ) {
                    actions()
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}
