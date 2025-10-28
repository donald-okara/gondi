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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ke.don.resources.Values
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
 * @param drawerContent Optional drawer content used when the layout is compact (modal drawer).
 * @param navigationRailContent Optional primary content for a navigation rail shown in non-compact layouts.
 * @param railHeader Optional header for the navigation rail.
 * @param railFooter Optional footer for the navigation rail.
 * @param drawerState State object controlling the drawer; defaults to closed.
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
    contentColor: Color = contentColorFor(containerColor),
    drawerContent: (@Composable ColumnScope.() -> Unit)? = null,
    navigationRailContent: (@Composable ColumnScope.() -> Unit)? = null,
    railHeader: @Composable (ColumnScope.() -> Unit)? = null,
    railFooter: @Composable (ColumnScope.() -> Unit)? = null,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (isCompact: Boolean) -> Unit,
) {
    val isCompact by remember { derivedStateOf { isCompact() } }

    val mainContent: @Composable (PaddingValues) -> Unit = remember(
        isCompact,
        navigationRailContent,
        railHeader,
        railFooter,
        content,
    ) {
        {
                padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = contentAlignment,
            ) {
                Row(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    if (!isCompact && (navigationRailContent != null || railHeader != null || railFooter != null)) {
                        NavigationRailToken(
                            modifier = Modifier.padding(Values.compactScreenPadding),
                            content = navigationRailContent,
                            header = railHeader,
                            footer = railFooter,
                            expanded = drawerState.isOpen,
                        )
                        VerticalDivider()
                    }

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

    if (isCompact && drawerContent != null) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = containerColor,
                    drawerContentColor = contentColor,
                    drawerShape = MaterialTheme.shapes.medium,
                ) {
                    drawerContent()
                }
            },
            content = scaffoldBody,
        )
    } else {
        scaffoldBody()
    }
}
