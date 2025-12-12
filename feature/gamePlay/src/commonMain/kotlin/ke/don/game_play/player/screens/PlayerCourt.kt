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
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedCourt
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
        myPlayer
    ) {
        derivedStateOf {
            votes.find { it.voterId == myPlayer.id }
        }
    }

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
    )

    if (playerState.showVote && accused != null) {
        CourtModal(
            modifier = Modifier,
            gameState = gameState,
            onEvent = onEvent,
            currentPlayer = myPlayer,
            selectedPlayer = accused!!,
            vote = vote
        )
    }
}
