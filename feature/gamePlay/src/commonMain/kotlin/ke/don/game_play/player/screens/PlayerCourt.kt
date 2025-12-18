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
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.player.components.CourtModal
import ke.don.game_play.player.components.CourtModalStrings
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedCourt
import ke.don.game_play.shared.SharedTownHallStrings
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun PlayerCourt(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    votes: List<Vote>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val accused by remember(
        gameState.accusedPlayer,
        players,
    ) {
        derivedStateOf {
            players.find { it.id == gameState.accusedPlayer?.targetId }
        }
    }
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

    val vote by remember(
        votes,
        myPlayer,
    ) {
        derivedStateOf {
            votes.find { it.voterId == myPlayer.id }
        }
    }

    val courtModalStrings = CourtModalStrings(
        dormantText = stringResource(Resources.Strings.GamePlay.ALREADY_VOTED_THIS_ROUND),
        deadPlayerText = stringResource(Resources.Strings.GamePlay.DEAD_MEN_TELL_NO_TALES),
        voteInnocentText = stringResource(Resources.Strings.GamePlay.VOTE_INNOCENT),
        voteGuiltyText = stringResource(Resources.Strings.GamePlay.VOTE_GUILTY),
        confirmInnocentText = { name -> Resources.Strings.GamePlay.playerIsInnocent(name) },
        confirmGuiltyText = { name -> Resources.Strings.GamePlay.condemnPlayer(name) },
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

    SharedCourt(
        players = players,
        onVote = {
            onEvent(PlayerHandler.ShowVoteDialog)
        },
        modifier = modifier,
        myPlayer = myPlayer,
        proceed = {},
        showRules = { onEvent(PlayerHandler.ShowRulesModal) },
        votes = votes,
        accused = accused,
        seconder = seconder,
        accuser = accuser,
        announcements = playerState.announcements,
        townHallStrings = townHallStrings,
        revealDeathsStrings = revealDeathsStrings,
    )

    if (playerState.showVote && accused != null) {
        CourtModal(
            modifier = Modifier,
            gameState = gameState,
            onEvent = onEvent,
            currentPlayer = myPlayer,
            selectedPlayer = accused!!,
            vote = vote,
            strings = courtModalStrings,
        )
    }
}
