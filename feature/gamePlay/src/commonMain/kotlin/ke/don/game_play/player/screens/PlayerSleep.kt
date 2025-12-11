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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.isActingInSleep
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.components.SleepModal
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedSleep

@Composable
fun PlayerSleep(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val currentRound = gameState.round
    val alivePlayers = players.filter { it.isAlive }

    val selectedPlayers by remember(gameState.lastSavedPlayerId, gameState.pendingKills, myPlayer) {
        derivedStateOf { gameState.selectedPlayersSleep(myPlayer) }
    }

    val instruction by remember(myPlayer.role) {
        derivedStateOf {
            when (myPlayer.role) {
                Role.GONDI -> "You are a Gondi, Pick a player to kill"
                Role.DOCTOR -> "You are a Doctor, Pick a player to save"
                Role.DETECTIVE -> "You are a Detective, Pick a player to investigate"
                else -> "Have a good rest, Please do not snore"
            }
        }
    }

    val isActing by remember(myPlayer.lastAction, myPlayer.role, currentRound) {
        derivedStateOf { isActingInSleep(myPlayer, currentRound) }
    }

    SharedSleep(
        modifier = modifier,
        myPlayerId = myPlayer.id,
        onSelectPlayer = {
            onEvent(PlayerHandler.SelectPlayer(it))
        },
        players = players,
        actingPlayers = if (isActing) listOf(myPlayer.id) else emptyList(),
        selectedPlayers = selectedPlayers,
        instruction = instruction,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        isModerator = false,
        onProceed = {},
        onShowRules = { onEvent(PlayerHandler.ShowRulesModal) },
        revealDeaths = playerState.revealDeaths,
        onDismiss = { onEvent(PlayerHandler.RevealDeaths) },
        lastAccused = null
    )

    val selectedPlayer by remember(playerState.selectedId, alivePlayers) {
        derivedStateOf {
            alivePlayers.find { it.id == playerState.selectedId }
        }
    }

    selectedPlayer?.let {
        SleepModal(
            modifier = Modifier,
            onEvent = onEvent,
            currentPlayer = myPlayer,
            selectedPlayer = it,
            gameState = gameState,
        )
    }
}
