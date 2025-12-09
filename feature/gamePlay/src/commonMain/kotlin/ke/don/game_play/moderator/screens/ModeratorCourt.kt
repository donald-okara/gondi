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
    )
}
