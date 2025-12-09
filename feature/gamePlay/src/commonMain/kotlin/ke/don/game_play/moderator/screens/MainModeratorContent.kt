/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.components.button.ComponentType
import ke.don.components.dialog.ConfirmationDialogToken
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.components.SelectedPlayerModal
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.shared.components.RulesModal
import ke.don.utils.capitaliseFirst
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModeratorContent(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    hostPlayer: Player?,
    players: List<Player>,
    votes: List<Vote>,
    onEvent: (ModeratorHandler) -> Unit,
    onBack: () -> Unit,
) {
    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back { onEvent(ModeratorHandler.ShowLeaveDialog) },
        title = gameState?.phase?.let { phase ->
            "${phase.name.capitaliseFirst()} for ${gameState.name}"
        } ?: "New game",
    ) {
        ContentSwitcher(
            moderatorState = moderatorState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            hostPlayer = hostPlayer,
        )
    }

    if (moderatorState.showRulesModal) {
        RulesModal { onEvent(ModeratorHandler.ShowRulesModal) }
    }

    if (moderatorState.showLeaveGame) {
        ConfirmationDialogToken(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Leave Game?",
            message = "You are about to leave the game you should understand that:",
            dialogType = ComponentType.Warning,
            checklist = listOf(
                "This game will be terminated and all progress will be lost.",
            ),
            onConfirm = onBack,
            onDismiss = { onEvent(ModeratorHandler.ShowLeaveDialog) },
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ContentSwitcher(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    players: List<Player>,
    hostPlayer: Player?,
    votes: List<Vote>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val selectedPlayer = players.find { it.id == moderatorState.selectedPlayerId }

    AnimatedContent(
        targetState = gameState?.phase,
        label = "Game State",
    ) { phase ->
        when (phase) {
            null -> {
                CreateGameContent(
                    modifier = modifier,
                    state = moderatorState,
                    onEvent = onEvent,
                )
            }
            GamePhase.LOBBY -> {
                ModeratorLobby(
                    modifier = modifier,
                    gameState = gameState,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = onEvent,
                    myPlayerId = hostPlayer?.id,
                )
            }
            GamePhase.SLEEP -> {
                if (gameState != null && hostPlayer != null) {
                    ModeratorSleep(
                        modifier = modifier,
                        gameState = gameState,
                        players = players,
                        myPlayer = hostPlayer,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.TOWN_HALL -> {
                if (gameState != null && hostPlayer != null) {
                    ModeratorTownHall(
                        modifier = modifier,
                        gameState = gameState,
                        moderatorState = moderatorState,
                        myPlayer = hostPlayer,
                        players = players,
                        onEvent = onEvent,
                    )
                }
            }
            GamePhase.COURT -> {
                if (gameState != null && hostPlayer != null) {
                    ModeratorCourt(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = hostPlayer,
                        players = players,
                        votes = votes,
                        onEvent = onEvent,
                        moderatorState = moderatorState,
                    )
                }
            }
            GamePhase.GAME_OVER -> {
                if (gameState != null && hostPlayer != null) {
                    ModeratorGameOver(
                        modifier = modifier,
                        gameState = gameState,
                        myPlayer = hostPlayer,
                        players = players,
                        onEvent = onEvent,
                    )
                }
            }
        }
    }

    if (selectedPlayer != null) {
        SelectedPlayerModal(
            onDismissRequest = { onEvent(ModeratorHandler.SelectPlayer(null)) },
            onAssignPlayer = {
                gameState?.let { it1 -> onEvent(ModeratorHandler.HandleModeratorCommand(ModeratorCommand.AssignRole(it1.id, selectedPlayer.id, it))) }
            },
            onRemovePlayer = {
                gameState?.let {
                    onEvent(
                        ModeratorHandler.HandleModeratorCommand(
                            ModeratorCommand.RemovePlayer(
                                it.id,
                                selectedPlayer.id,
                            ),
                        ),
                    )
                }
            },
            player = selectedPlayer,
        )
    }
}
