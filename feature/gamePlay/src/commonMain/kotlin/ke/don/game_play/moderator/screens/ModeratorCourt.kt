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
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.state.nextPhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.shared.SharedCourt
import ke.don.game_play.shared.SharedTownHallStrings
import ke.don.game_play.shared.components.RevealDeathsStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Composable
fun ModeratorCourt(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    votes: List<Vote>,
    moderatorState: ModeratorState,
    onEvent: (ModeratorHandler) -> Unit,
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
        onVote = {},
        modifier = modifier,
        myPlayer = myPlayer,
        proceed = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameState.id,
                        gameState.phase.nextPhase,
                    ),
                ),
            )
        },
        showRules = { onEvent(ModeratorHandler.ShowRulesModal) },
        votes = votes,
        accused = accused,
        isModerator = true,
        seconder = seconder,
        accuser = accuser,
        announcements = moderatorState.announcements,
        townHallStrings = townHallStrings,
        revealDeathsStrings = revealDeathsStrings,
    )
}
