/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.components.empty_state.EmptyState
import ke.don.components.empty_state.EmptyType
import ke.don.components.indicator.FancyLoadingIndicator
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.design.theme.spacing
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.components.RulesModal
import ke.don.resources.Resources
import ke.don.utils.capitaliseFirst
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.isError
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Immutable
data class MainPlayerContentStrings(
    val leaveGameTitle: String,
    val leaveGameMessage: String,
    val leaveGameChecklist: List<String>,
    val connecting: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPlayerContent(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    gameState: GameState? = null,
    currentPlayer: Player?,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (PlayerHandler) -> Unit,
    onBack: () -> Unit,
) {
    val strings = MainPlayerContentStrings(
        leaveGameTitle = stringResource(Resources.Strings.GamePlay.LEAVE_GAME_TITLE),
        leaveGameMessage = stringResource(Resources.Strings.GamePlay.LEAVE_GAME_MESSAGE),
        leaveGameChecklist = listOf(
            stringResource(Resources.Strings.GamePlay.LEAVE_GAME_CHECKLIST_1),
            stringResource(Resources.Strings.GamePlay.LEAVE_GAME_CHECKLIST_2),
        ),
        connecting = stringResource(Resources.Strings.GamePlay.CONNECTING),
    )

    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back {
            if (playerState.connectionStatus.isError) {
                onBack()
            } else {
                onEvent(PlayerHandler.ShowLeaveDialog)
            }
        },
        title = gameState?.phase?.let { phase ->
            "${phase.name.capitaliseFirst()} for ${gameState.name}"
        } ?: strings.connecting,
    ) {
        ContentSwitcher(
            playerState = playerState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            currentPlayer = currentPlayer,
            onBack = onBack,
        )
    }

    if (playerState.showRulesModal) {
        RulesModal { onEvent(PlayerHandler.ShowRulesModal) }
    }

    if (playerState.showLeaveGame) {
        ConfirmationDialogToken(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = strings.leaveGameTitle,
            message = strings.leaveGameMessage,
            dialogType = ComponentType.Warning,
            checklist = strings.leaveGameChecklist,
            onConfirm = onBack,
            onDismiss = { onEvent(PlayerHandler.ShowLeaveDialog) },
        )
    }
}

@Immutable
data class LoadingStateStrings(
    val connecting: String,
)

@Immutable
data class ErrorStateStrings(
    val title: String,
    val description: (String) -> String,
    val leaveGame: String,
)

@OptIn(ExperimentalTime::class)
@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    gameState: GameState? = null,
    players: List<Player>,
    currentPlayer: Player?,
    votes: List<Vote>,
    onEvent: (PlayerHandler) -> Unit,
    onBack: () -> Unit,
) {
    val loadingStateStrings = LoadingStateStrings(
        connecting = stringResource(Resources.Strings.GamePlay.CONNECTING),
    )
    val errorStateStrings = ErrorStateStrings(
        title = stringResource(Resources.Strings.GamePlay.SOMETHING_WENT_WRONG),
        description = { Resources.Strings.GamePlay.gameDoesntExist(error = it) },
        leaveGame = stringResource(Resources.Strings.GamePlay.LEAVE_GAME),
    )

    AnimatedContent(
        targetState = gameState?.phase to playerState.connectionStatus,
        label = "Game State",
    ) { (phase, connectionStatus) ->
        when (phase) {
            null -> {
                when (connectionStatus) {
                    is ReadStatus.Error -> ErrorState(
                        error = connectionStatus.message,
                        leave = {
                            if (playerState.connectionStatus.isError) {
                                onBack()
                            } else {
                                onEvent(PlayerHandler.ShowLeaveDialog)
                            }
                        },
                        strings = errorStateStrings,
                    )
                    else ->
                        LoadingState(
                            modifier = modifier,
                            strings = loadingStateStrings,
                        )
                }
            }
            GamePhase.LOBBY -> {
                PlayerLobby(
                    modifier = modifier,
                    players = players,
                    myPlayerId = currentPlayer?.id,
                    gameState = gameState,
                    playerState = playerState,
                    onEvent = onEvent,
                )
            }
            GamePhase.SLEEP -> {
                if (gameState != null && currentPlayer != null) {
                    PlayerSleep(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = currentPlayer,
                        players = players,
                        playerState = playerState,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.TOWN_HALL -> {
                if (gameState != null && currentPlayer != null) {
                    PlayerTownHall(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = currentPlayer,
                        players = players,
                        playerState = playerState,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.COURT -> {
                if (gameState != null && currentPlayer != null) {
                    PlayerCourt(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = currentPlayer,
                        players = players,
                        votes = votes,
                        playerState = playerState,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.GAME_OVER -> {
                if (gameState != null && currentPlayer != null) {
                    PlayerGameOver(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = currentPlayer,
                        players = players,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
    strings: LoadingStateStrings,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.width(MaterialTheme.spacing.largeScreenSize),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
            ) {
                FancyLoadingIndicator(loading = true)
                Text(
                    text = strings.connecting,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    error: String,
    leave: () -> Unit,
    strings: ErrorStateStrings,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyState(
            title = strings.title,
            description = strings.description(error),
            emptyType = EmptyType.Error,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
        ) {
            ButtonToken(
                buttonType = ComponentType.Error,
                onClick = leave,
            ) {
                Text(
                    text = strings.leaveGame,
                )
            }
        }
    }
}
