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
import ke.don.game_play.player.components.SleepModalStrings
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedSleep
import ke.don.game_play.shared.SharedSleepStrings
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import ke.don.utils.formatArgs
import org.jetbrains.compose.resources.stringResource

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
    val gondiInstruction = stringResource(Resources.Strings.GamePlay.GONDI_INSTRUCTION)
    val doctorInstruction = stringResource(Resources.Strings.GamePlay.DOCTOR_INSTRUCTION)
    val detectiveInstruction = stringResource(Resources.Strings.GamePlay.DETECTIVE_INSTRUCTION)
    val defaultInstruction = stringResource(Resources.Strings.GamePlay.DEFAULT_INSTRUCTION)

    val instruction by remember(myPlayer.role) {
        derivedStateOf {
            when (myPlayer.role) {
                Role.GONDI -> gondiInstruction
                Role.DOCTOR -> doctorInstruction
                Role.DETECTIVE -> detectiveInstruction
                else -> defaultInstruction
            }
        }
    }

    val isActing by remember(myPlayer.lastAction, myPlayer.role, currentRound) {
        derivedStateOf { isActingInSleep(myPlayer, currentRound) }
    }

    val lastAccused = remember(gameState.accusedPlayer) { gameState.accusedPlayer?.targetId }

    // Extract string resources into vals first
    val gondiConfirmationString = stringResource(Resources.Strings.GamePlay.CONFIRMATION_GONDI)
    val doctorConfirmationString = stringResource(Resources.Strings.GamePlay.CONFIRMATION_DOCTOR)
    val detectiveConfirmationString = stringResource(Resources.Strings.GamePlay.CONFIRMATION_DETECTIVE)
    val gondiDormantString = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_GONDI)
    val doctorDormantString = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DOCTOR)
    val detectiveDormantString = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DETECTIVE)
    val defaultDormantString = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DEFAULT)

    // Now, create the SleepModalStrings instance using the vals
    val sleepModalStrings = SleepModalStrings(
        gondiConfirmation = { name -> gondiConfirmationString.formatArgs(name) },
        doctorConfirmation = { name -> doctorConfirmationString.formatArgs(name) },
        detectiveConfirmation = { name -> detectiveConfirmationString.formatArgs(name) },
        gondiDormant = { name -> gondiDormantString.formatArgs(name) },
        doctorDormant = { name -> doctorDormantString.formatArgs(name) },
        detectiveDormant = { name -> detectiveDormantString.formatArgs(name) },
        defaultDormant = { name -> defaultDormantString.formatArgs(name) },
    )

    val sharedSleepStrings = SharedSleepStrings(
        proceed = stringResource(Resources.Strings.GamePlay.PROCEED),
        showRules = stringResource(Resources.Strings.GamePlay.SHOW_RULES),
    )
    val revealDeathsStrings = RevealDeathsStrings(
        nightResultsTitle = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS),
        courtRulingTitle = stringResource(Resources.Strings.GamePlay.COURT_RULING),
        nightResultsDescription = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS_DESCRIPTION),
        courtRulingDescription = stringResource(Resources.Strings.GamePlay.COURT_RULING_DESCRIPTION),
        killedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.KILLED_PLAYER),
        savedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.SAVED_PLAYER),
        eliminatedPlayerMessage = { role -> Resources.Strings.GamePlay.eliminatedPlayer(role) },
        savedBySaviourMessage = { saviour -> Resources.Strings.GamePlay.savedBySaviour(saviour) },
        courtRulingText = stringResource(Resources.Strings.GamePlay.COURT_RULING),
        theDoctorText = stringResource(Resources.Strings.GamePlay.THE_DOCTOR),
    )

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
        lastAccused = lastAccused,
        strings = sharedSleepStrings,
        revealDeathsStrings = revealDeathsStrings,
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
            strings = sleepModalStrings,
        )
    }
}
