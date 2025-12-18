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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.shared.SharedGameOver
import ke.don.game_play.shared.SharedGameOverStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModeratorGameOver(
    modifier: Modifier = Modifier,
    myPlayer: Player,
    gameState: GameState,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val winningFaction = remember { gameState.winners } ?: return

    val strings = SharedGameOverStrings(
        gondiWinRemark = stringResource(Resources.Strings.GamePlay.GONDI_WIN_REMARK),
        villagerWinRemark = stringResource(Resources.Strings.GamePlay.VILLAGER_WIN_REMARK),
        gondiWin = stringResource(Resources.Strings.GamePlay.GONDIS_WIN),
        villagersWin = stringResource(Resources.Strings.GamePlay.VILLAGERS_WIN),
        playAgain = stringResource(Resources.Strings.GamePlay.PLAY_AGAIN),
    )

    SharedGameOver(
        modifier = modifier,
        isModerator = true,
        players = players,
        myPlayer = myPlayer,
        winnerFaction = winningFaction,
        playAgain = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameId = gameState.id,
                        phase = GamePhase.LOBBY,
                    ),
                ),
            )
        },
        strings = strings,
    )
}
