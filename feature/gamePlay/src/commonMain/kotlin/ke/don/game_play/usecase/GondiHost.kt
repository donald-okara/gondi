/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.usecase

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GondiHost(
    private val server: LocalServer,
    private val database: LocalDatabase,
): ScreenModel {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes


    private fun observeDatabase(gameId: String) {
        screenModelScope.launch {
            database.getGameState(gameId).collect { rows ->
                _gameState.value = rows
            }
        }

        screenModelScope.launch {
            database.getAllPlayers().collect { rows ->
                _players.value = rows
            }
        }

        screenModelScope.launch {
            database.getAllVotes().collect { rows ->
                _votes.value = rows
            }
        }
    }

    // Moderator actions
    suspend fun handleIntent(intent: ModeratorCommand) = server.handleModeratorCommand(gameState.value?.id ?: error("Game state cannot be null"), intent)
}
