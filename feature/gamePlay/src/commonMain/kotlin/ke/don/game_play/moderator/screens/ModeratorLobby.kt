/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
@file:OptIn(ExperimentalTime::class)

package ke.don.game_play.moderator.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.components.SelectedPlayerModal
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.shared.SharedLobby
import kotlin.time.ExperimentalTime

@Composable
fun ModeratorLobby(
    modifier: Modifier = Modifier,
    gameState: GameState? = null,
    myPlayerId: String? = null,
    moderatorState: ModeratorState,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val selectedPlayer = players.find { it.id == moderatorState.selectedPlayerId }

    SharedLobby(
        modifier = modifier,
        isModerator = true,
        gameState = gameState,
        myPlayerId = myPlayerId,
        players = players,
        onSelectPlayer = {
            onEvent(ModeratorHandler.SelectPlayer(it))
        },
        announcements = moderatorState.announcements,
        startGame = {
            gameState?.id?.let {
                onEvent(
                    ModeratorHandler.HandleModeratorCommand(
                        ModeratorCommand.StartGame(it),
                    ),
                )
            }
        },
        onShowRules = {
            onEvent(ModeratorHandler.ShowRulesModal)
        }
    )

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
