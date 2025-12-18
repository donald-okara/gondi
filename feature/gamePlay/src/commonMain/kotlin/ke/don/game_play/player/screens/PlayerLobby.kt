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
import androidx.compose.ui.Modifier
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedLobby
import ke.don.game_play.shared.SharedLobbyStrings
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PlayerLobby(
    modifier: Modifier = Modifier,
    gameState: GameState? = null,
    myPlayerId: String? = null,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val strings = SharedLobbyStrings(
        readyToBegin = stringResource(Resources.Strings.GamePlay.READY_TO_BEGIN),
        startGame = stringResource(Resources.Strings.GamePlay.START_GAME),
        waitingForMorePlayers = stringResource(Resources.Strings.GamePlay.WAITING_FOR_MORE_PLAYERS),
        roleLockWarning = stringResource(Resources.Strings.GamePlay.ROLE_LOCK_WARNING),
        moderatorPanel = stringResource(Resources.Strings.GamePlay.MODERATOR_PANEL),
        showRules = stringResource(Resources.Strings.GamePlay.SHOW_RULES),
        playersInLobby = stringResource(Resources.Strings.GamePlay.PLAYERS_IN_LOBBY),
        startWithPlayers = { count -> Resources.Strings.GamePlay.startWithPlayers(count) },
    )
    SharedLobby(
        modifier = modifier,
        isModerator = false,
        gameState = gameState,
        myPlayerId = myPlayerId,
        players = players,
        announcements = playerState.announcements,
        onShowRules = {
            onEvent(PlayerHandler.ShowRulesModal)
        },
        strings = strings,
    )
}
