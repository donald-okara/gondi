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
import ke.don.game_play.shared.SharedSleepStrings
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

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
        stringResource(Resources.Strings.GamePlay.PROCEED_TO_TOWN_HALL)
    } else {
        stringResource(Resources.Strings.GamePlay.SOMEONE_HAS_NOT_DONE_THEIR_PART_YET)
    }

    val lastAccused = remember(gameState.accusedPlayer) { gameState.accusedPlayer?.targetId }

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
        lastAccused = lastAccused,
        strings = sharedSleepStrings,
        revealDeathsStrings = revealDeathsStrings,
    )
}
