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
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.components.TownHallModal
import ke.don.game_play.player.components.TownHallModalStrings
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedTownHall
import ke.don.game_play.shared.SharedTownHallStrings
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PlayerTownHall(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val accuser by remember(
        gameState.accusedPlayer,
        players,
    ) {
        derivedStateOf {
            players.find { it.id == gameState.accusedPlayer?.playerId }
        }
    }

    val seconder by remember(
        gameState.second,
        players,
    ) {
        derivedStateOf {
            players.find { it.id == gameState.second?.playerId }
        }
    }

    val accused by remember(
        gameState.accusedPlayer,
        players,
    ) {
        derivedStateOf {
            players.find { it.id == gameState.accusedPlayer?.targetId }
        }
    }

    val townHallModalStrings = TownHallModalStrings(
        alreadyOnTrial = stringResource(Resources.Strings.GamePlay.ALREADY_ON_TRIAL),
        accusePlayerConfirmation = { name -> Resources.Strings.GamePlay.accusePlayerConfirmation(name) },
    )

    val townHallStrings = SharedTownHallStrings(
        sessionOver = stringResource(Resources.Strings.GamePlay.SESSION_OVER),
        proceed = stringResource(Resources.Strings.GamePlay.PROCEED),
        goToCourt = stringResource(Resources.Strings.GamePlay.GO_TO_COURT),
        noAccusations = stringResource(Resources.Strings.GamePlay.NO_ACCUSATIONS),
        vote = stringResource(Resources.Strings.GamePlay.VOTE),
        noSeconder = { name -> Resources.Strings.GamePlay.noSeconder(name) },
        exoneratePlayer = { name -> Resources.Strings.GamePlay.exoneratePlayer(name) },
        isPlayerGuilty = { name -> Resources.Strings.GamePlay.isPlayerGuilty(name) },
    )

    val revealDeathsStrings = RevealDeathsStrings(
        nightResultsTitle = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS),
        courtRulingTitle = stringResource(Resources.Strings.GamePlay.COURT_RULING),
        nightResultsDescription = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS_DESCRIPTION),
        courtRulingDescription = stringResource(Resources.Strings.GamePlay.COURT_RULING_DESCRIPTION),
        killedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.KILLED_PLAYER),
        savedPlayerContentDescription = stringResource(Resources.Strings.GamePlay.SAVED_PLAYER),
        eliminatedPlayerMessage = { name -> Resources.Strings.GamePlay.eliminatedPlayer(name) },
        savedBySaviourMessage = { name -> Resources.Strings.GamePlay.savedBySaviour(name) },
        courtRulingText = stringResource(Resources.Strings.GamePlay.COURT_RULING),
        theDoctorText = stringResource(Resources.Strings.GamePlay.THE_DOCTOR),
    )

    SharedTownHall(
        players = players,
        onSelectPlayer = { onEvent(PlayerHandler.SelectPlayer(it)) },
        myPlayerId = myPlayer.id,
        seconder = seconder,
        accuser = accuser,
        accused = accused,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        onSecond = { accused?.id?.let { onEvent(PlayerHandler.Send(PlayerIntent.Second(myPlayer.id, gameState.round, it))) } },
        proceed = {},
        exoneratePlayer = {},
        onShowRules = { onEvent(PlayerHandler.ShowRulesModal) },
        isModerator = false,
        announcements = playerState.announcements,
        modifier = modifier,
        revealDeaths = playerState.revealDeaths,
        onDismiss = { onEvent(PlayerHandler.RevealDeaths) },
        lastSaved = gameState.lastSavedPlayerId,
        lastKilled = gameState.pendingKills.filter { id -> players.find { it.id == id }?.isAlive == false }, // Only show players whose death has been processed
        townHallStrings = townHallStrings,
        revealDeathsStrings = revealDeathsStrings,
    )

    val selectedPlayer by remember(playerState.selectedId, players) {
        derivedStateOf {
            players.find { it.id == playerState.selectedId }
        }
    }
    if (selectedPlayer != null) {
        TownHallModal(
            modifier = Modifier,
            gameState = gameState,
            onEvent = onEvent,
            currentPlayer = myPlayer,
            selectedPlayer = selectedPlayer!!,
            strings = townHallModalStrings,
        )
    }
}
