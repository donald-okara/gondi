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
