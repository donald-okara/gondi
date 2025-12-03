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
import ke.don.utils.capitaliseFirst
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.isError

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
        } ?: "Connecting ...",
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
            title = "Leave Game?",
            message = "You are about to leave the game you should understand that:",
            dialogType = ComponentType.Warning,
            checklist = listOf(
                "Your progress will be lost",
                "You cannot come back to the game",
            ),
            onConfirm = onBack,
            onDismiss = { onEvent(PlayerHandler.ShowLeaveDialog) },
        )
    }
}

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
                    )
                    else ->
                        LoadingState(
                            modifier = modifier,
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
                gameState?.let {
                    PlayerSleep(
                        modifier = modifier,
                        gameState = it,
                        myPlayer = currentPlayer!!,
                        players = players,
                        playerState = playerState,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.TOWN_HALL -> {}
            GamePhase.COURT -> {}
            GamePhase.GAME_OVER -> {}
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
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
                    text = "Connecting...",
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
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyState(
            title = "Something went wrong",
            description = "$error. The game most likely doesn't exist anymore.",
            emptyType = EmptyType.Error,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
        ) {
            ButtonToken(
                buttonType = ComponentType.Error,
                onClick = leave,
            ) {
                Text(
                    text = "Leave game",
                )
            }
        }
    }
}
