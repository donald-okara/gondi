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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.shared.SharedGameOver

@Composable
fun PlayerGameOver(
    modifier: Modifier = Modifier,
    myPlayer: Player,
    gameState: GameState,
    players: List<Player>,
) {
    val winningFaction = remember { gameState.winners } ?: return

    SharedGameOver(
        modifier = modifier,
        isModerator = false,
        players = players,
        myPlayer = myPlayer,
        winnerFaction = winningFaction,
        playAgain = {},
    )
}
