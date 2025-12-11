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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.isActingInSleep
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.nextPhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.shared.SharedSleep

@Composable
fun ModeratorSleep(
    modifier: Modifier = Modifier,
    gameState: GameState,
    moderatorState: ModeratorState,
    myPlayer: Player,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val currentRound = gameState.round
    val actingPlayers by remember(players, currentRound) {
        derivedStateOf {
            players.filter { player ->
                isActingInSleep(player, currentRound)
            }.map { it.id }
        }
    }

    val selectedPlayers by remember(gameState.lastSavedPlayerId, gameState.pendingKills) {
        derivedStateOf { gameState.selectedPlayersSleep() }
    }

    val instruction = if (actingPlayers.isEmpty()) {
        "You can now proceed to Town hall"
    } else {
        "Someone has not done their part yet"
    }

    SharedSleep(
        modifier = modifier,
        myPlayerId = myPlayer.id,
        onSelectPlayer = {
            onEvent(ModeratorHandler.SelectPlayer(it))
        },
        players = players,
        selectedPlayers = selectedPlayers,
        instruction = instruction,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        isModerator = true,
        enabled = actingPlayers.isEmpty(),
        onProceed = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameState.id,
                        gameState.phase.nextPhase,
                    ),
                ),
            )
        },
        actingPlayers = actingPlayers,
        onShowRules = { onEvent(ModeratorHandler.ShowRulesModal) },
        revealDeaths = moderatorState.revealDeaths,
        onDismiss = {
            onEvent(ModeratorHandler.RevealDeaths)
        },
        lastAccused = null
    )
}
