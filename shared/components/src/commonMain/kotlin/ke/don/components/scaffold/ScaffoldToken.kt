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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import ke.don.resources.isCompact

/**
 * Renders a responsive scaffold that adapts between compact (modal drawer) and expanded (navigation rail) layouts,
 * and provides configurable top bar, floating action button, drawer, and navigation rail areas.
 *
 * The provided `content` composable receives a boolean flag indicating the current compact state so it can
 * adapt its UI.
 *
 * @param modifier Modifier applied to the scaffold root.
 * @param title Optional title used by the default top bar.
 * @param navigationIcon Navigation icon configuration for the default top bar.
 * @param actions Slot for top bar action content.
 * @param scrollBehavior Optional scroll behavior forwarded to the top bar.
 * @param topBar Optional custom top bar composable; if null no top bar is shown. A default top bar is used when not provided.
 * @param floatingActionButton Optional FAB content.
 * @param floatingActionButtonPosition Position of the floating action button.
 * @param containerColor Background color of the scaffold.
 * @param contentColor Preferred content color for scaffold children.
 * @param contentAlignment Alignment for the main content within the scaffold.
 * @param content Main content composable; receives `isCompact: Boolean` where `true` indicates compact layout mode.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldToken(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: NavigationIcon = NavigationIcon.None,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    topBar: (@Composable () -> Unit)? = {
        TopBarToken(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
        )
    },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    scrollState: ScrollState,
    contentColor: Color = contentColorFor(containerColor),
    verticalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.medium),
    horizontalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.small),
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable ColumnScope.(isCompact: Boolean) -> Unit,
) {
    ScaffoldToken(
        modifier = modifier
            .verticalScroll(scrollState),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        contentAlignment = contentAlignment,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldToken(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: NavigationIcon = NavigationIcon.None,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    topBar: (@Composable () -> Unit)? = {
        TopBarToken(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
        )
    },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    verticalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.medium),
    horizontalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.small),
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable ColumnScope.(isCompact: Boolean) -> Unit,
) {
    val isCompact by remember { derivedStateOf { isCompact() } }

    val mainContent: @Composable (PaddingValues) -> Unit = remember(
        isCompact,
        content,
    ) {
        @Composable { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = contentAlignment,
            ) {
                Column(
                    modifier = modifier
                        .padding(
                            spacingPaddingValues(
                                vertical = verticalPadding,
                                horizontal = horizontalPadding,
                            ),
                        )
                        .width(MaterialTheme.spacing.largeScreenSize),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.Top),
                ) {
                    content(isCompact)
                }
            }
        }
    }

    val scaffoldBody: @Composable () -> Unit = remember(
        modifier,
        topBar,
        floatingActionButton,
        floatingActionButtonPosition,
        containerColor,
        contentColor,
        mainContent,
    ) {
        {
            Scaffold(
                topBar = { topBar?.invoke() },
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentColor = contentColor,
                content = mainContent,
            )
        }
    }

    scaffoldBody()
}
