package ke.don.home.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.components.empty_state.EmptyState
import ke.don.components.empty_state.EmptyType
import ke.don.components.icon.IconToken
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.list_items.DropDownData
import ke.don.components.list_items.DropDownToken
import ke.don.components.profile.ProfileImageToken
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.RefreshLazyColumn
import ke.don.components.scaffold.ScaffoldToken
import ke.don.components.scaffold.cardCrunchEffects
import ke.don.design.theme.spacing
import ke.don.home.components.GameRoomItem
import ke.don.home.components.ThemeSheet
import ke.don.home.model.HomeIntentHandler
import ke.don.home.model.HomeState
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.isRefreshing
import ke.don.utils.result.message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeIntentHandler) -> Unit,
    navigateToAuth: () -> Unit
) {
    val profileMenuItems = listOf(
        DropDownData(
            title = "Edit",
            onClick = { onEvent(HomeIntentHandler.NavigateToEdit) }
        ),
        DropDownData(
            title = "Log out",
            destructive = true,
            onClick = { onEvent(HomeIntentHandler.ShowLogoutModal) }
        )
    )

    val menuItems = listOf(
        DropDownData(
            title = "Rules",
            onClick = { onEvent(HomeIntentHandler.NavigateToRules) }
        ),
        DropDownData(
            title = "Theme",
            onClick = { onEvent(HomeIntentHandler.ShowThemeModal) }
        )
    )

    ScaffoldToken(
        modifier = modifier,
        title = "Gondi",
        actions = {
            state.profile.let { profile ->
                Box{
                    ProfileImageToken(
                        isHero = false,
                        profile = profile,
                        onClick = {onEvent(HomeIntentHandler.ShowProfileMenu)},
                    )

                    DropDownToken(
                        items = profileMenuItems,
                        expanded = state.showProfileMenu,
                        onDismiss = { onEvent(HomeIntentHandler.ShowProfileMenu) },
                    )
                }
            }
        },
        floatingActionButton = {
            ButtonToken(
                onClick = { onEvent(HomeIntentHandler.NavigateToNewGame)},
                buttonType = ComponentType.Primary
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add game"
                )
                Text(text = "New Game")
            }
        },
        navigationIcon = NavigationIcon.Custom {
            Box {
                IconToken(
                    imageVector = Icons.Default.Menu,
                    buttonType = ComponentType.Neutral,
                    onClick = { onEvent(HomeIntentHandler.ShowMenu) }
                )

                DropDownToken(
                    items = menuItems,
                    expanded = state.showMenu,
                    onDismiss = { onEvent(HomeIntentHandler.ShowMenu)}
                )
            }
        }
    ) {
        val displayStatus = when (state.readStatus) {
            is ReadStatus.Success,
            is ReadStatus.Refreshing -> ReadStatus.Success
            else -> state.readStatus
        }

        AnimatedContent(
            targetState = displayStatus,
            label = "HomeContent"
        ) { status ->
            when (status) {
                is ReadStatus.Success -> SuccessState(
                    state = state,
                    onEvent = onEvent
                )
                is ReadStatus.Error -> ErrorState(
                    state = state,
                    onEvent = onEvent
                )
                ReadStatus.Loading -> LoadingState()
                ReadStatus.Empty -> EmptyState(onEvent = onEvent)
                else -> {}
            }
        }
    }

    if (state.showLogoutModal){
        ConfirmationDialogToken(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Log out",
            message = "Are you sure you want to log out",
            dialogType = ComponentType.Warning,
            onConfirm = { onEvent(HomeIntentHandler.LogOut(navigateToAuth)) },
            onDismiss ={ onEvent(HomeIntentHandler.ShowLogoutModal) }
        )
    }

    if (state.showThemeModal){
        ThemeSheet(
            onDismissRequest = { onEvent(HomeIntentHandler.ShowThemeModal) },
            onThemeChange = { onEvent(HomeIntentHandler.SetTheme(it)) },
            currentTheme = state.theme
        )
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeIntentHandler) -> Unit,
) {
    val pullState = rememberPullToRefreshState()
    RefreshLazyColumn(
        modifier = modifier,
        isRefreshing = state.readStatus.isRefreshing,
        pullRefreshState = pullState,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        onRefresh = { onEvent(HomeIntentHandler.Refresh) },
    ){
        items(state.games.size){ index ->
            GameRoomItem(
                gameIdentity = state.games[index],
                onClick = {
                    onEvent(
                        HomeIntentHandler.NavigateToGame(state.games[index].serviceHost to
                                state.games[index].servicePort)
                    )
                },
                modifier = Modifier.cardCrunchEffects(
                    isRefreshing = state.readStatus.isRefreshing,
                    pullProgress = pullState.distanceFraction,
                    index = index
                )
            )
        }
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeIntentHandler) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptyState(
            title = "Something went wrong",
            description = state.readStatus.message,
            emptyType = EmptyType.Error
        ) {
            ButtonToken(
                onClick = { onEvent(HomeIntentHandler.DiscoverGames) },
                buttonType = ComponentType.Primary
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    onEvent: (HomeIntentHandler) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        EmptyState(
            icon = Icons.Default.Casino,
            title = "Start a New Adventure?",
            description = "It looks a bit quiet around here. " +
                    "There are currently no active games available to join." +
                    " Why not check your network, or better yet, be the first to start one?",
            emptyType = EmptyType.Empty
        ){
            ButtonToken(
                onClick = { onEvent(HomeIntentHandler.ShowNetworkChooser) },
                buttonType = ComponentType.Inverse
            ){
                Text(text = "Check Network")
            }

            ButtonToken(
                onClick = { onEvent(HomeIntentHandler.DiscoverGames) },
                buttonType = ComponentType.Inverse
            ){
                Text(text = "Refresh Screen")
            }

            ButtonToken(
                onClick = { onEvent(HomeIntentHandler.NavigateToNewGame) },
                buttonType = ComponentType.Primary
            ){
                Text(text = "Create New Game")
            }
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.width(MaterialTheme.spacing.largeScreenSize),
            contentAlignment = Alignment.Center,
        ) {
            FancyLoadingIndicator(loading = true)
        }
    }
}