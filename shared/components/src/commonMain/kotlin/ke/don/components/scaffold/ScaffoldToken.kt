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

import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
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
import kotlin.math.roundToInt


/**
 * A convenience overload of `ScaffoldToken` for vertically scrollable content.
 *
 * This overload accepts a `ScrollState` and applies a `verticalScroll` modifier to the underlying
 * `Column` content, making the entire content area scrollable.
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
 * @param scrollState The `ScrollState` to be used for the vertical scroll behavior of the content.
 * @param contentColor Preferred content color for scaffold children.
 * @param verticalPadding The vertical padding to be applied around the content.
 * @param horizontalPadding The horizontal padding to be applied around the content.
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

/**
 * An overload of `ScaffoldToken` that integrates a `LazyColumn` with pull-to-refresh functionality.
 *
 * This composable builds upon the standard `Scaffold` to provide a ready-made structure for
 * displaying a list of items that can be refreshed by the user. It wraps the content in a
 * `RefreshLazyColumn`, handling the pull-to-refresh state and UI automatically.
 *
 * @param modifier The modifier to be applied to the `Scaffold` element.
 * @param title An optional title string to be displayed in the default top bar.
 * @param navigationIcon The navigation icon configuration for the default top bar.
 * @param actions A composable lambda for actions to be displayed in the top bar's `RowScope`.
 * @param scrollBehavior An optional `TopAppBarScrollBehavior` to be applied to the default top bar,
 * allowing it to react to scroll events.
 * @param topBar A custom composable for the top app bar. If null, a default `TopBarToken` is used.
 * @param floatingActionButton A composable lambda for the floating action button.
 * @param floatingActionButtonPosition The position of the floating action button on the screen.
 * @param containerColor The background color for the `Scaffold` container.
 * @param contentColor The preferred color for content inside the `Scaffold`.
 * @param isRefreshing A boolean indicating if the refresh indicator should be displayed.
 * @param onRefresh A lambda to be invoked when a refresh is triggered by the user.
 * @param listOffset An optional integer to override the calculated vertical offset of the list content,
 * used for custom animations or positioning.
 * @param pullRefreshState The state object that controls and observes the pull-to-refresh mechanism.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
            scrollBehavior = scrollBehavior
        )
    },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    pullRefreshState: PullRefreshState,
    lazyListState: LazyListState = rememberLazyListState(),
    verticalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.medium),
    horizontalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.small),
    listOffset: Int = rememberOffset(
        isRefreshing = isRefreshing,
        pullProgress = pullRefreshState.progress
    ),
    contentAlignment: Alignment = Alignment.TopCenter,
    content: LazyListScope.(isCompact: Boolean) -> Unit
) {
    val isCompact = isCompact()

    val mainContent: @Composable (PaddingValues) -> Unit = remember(
        isCompact,
        listOffset,
        lazyListState,
        pullRefreshState,
        isRefreshing,
    ) {
        { padding ->
            RefreshLazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                lazyListState = lazyListState,
                listOffSet = listOffset,
                verticalPadding = verticalPadding,
                pullRefreshState = pullRefreshState,
                horizontalPadding = horizontalPadding,
                contentAlignment = contentAlignment,
            ) {
                content(isCompact)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { topBar?.invoke() },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        content = mainContent
    )
}


/**
 * A composable that provides a foundational layout structure, wrapping Material 3's `Scaffold`.
 * It standardizes the app's screen layout by providing a consistent top bar, content area, and
 * floating action button configuration. The content is placed within a `Column` that is centered
 * horizontally and constrained in width, suitable for large screens.
 *
 * This overload is for non-scrollable content. For scrollable or lazy-loading content,
 * use the other overloads of `ScaffoldToken`.
 *
 * The `content` lambda receives a boolean `isCompact` which can be used to adapt the layout
 * based on the available screen width.
 *
 * @param modifier The modifier to be applied to the main content `Column`.
 * @param title An optional title string to be displayed in the default `TopBarToken`.
 * @param navigationIcon Defines the navigation icon for the default `TopBarToken` (e.g., a back arrow or menu icon).
 * @param actions A composable lambda for actions to be displayed at the end of the `TopBarToken`.
 * @param scrollBehavior An optional `TopAppBarScrollBehavior` to coordinate scrolling between the top bar and content.
 * @param topBar A custom composable for the top app bar. If null, no top bar is displayed. Defaults to `TopBarToken`.
 * @param floatingActionButton A composable for the floating action button.
 * @param floatingActionButtonPosition The position of the `floatingActionButton`.
 * @param containerColor The background color for the `Scaffold`.
 * @param contentColor The preferred color for content inside the `Scaffold`.
 * @param verticalPadding The vertical padding to be applied around the main content `Column`.
 * @param horizontalPadding The horizontal padding to be applied around the main content `Column`.
 * @param contentAlignment The alignment of the main content `Column` within the `Scaffold`.
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
