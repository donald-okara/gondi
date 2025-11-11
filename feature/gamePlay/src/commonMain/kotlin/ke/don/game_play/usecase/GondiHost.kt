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
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GondiHost(
    private val server: LocalServer,
    private val database: LocalDatabase,
) : ScreenModel {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _hostPlayer = MutableStateFlow<Player?>(null) // TODO: Update this to populate from datastore once that task is complete
    val hostPlayer: StateFlow<Player?> = _hostPlayer

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes

    private val _createGameState = MutableStateFlow(GameState())
    val createGameState: StateFlow<GameState> = _createGameState

    private var dbObserveJob: Job? = null

    private fun observeDatabase(gameId: String) {
        // Cancel any previous observation
        dbObserveJob?.cancel()

        dbObserveJob = screenModelScope.launch {
            combine(
                database.getGameState(gameId),
                database.getAllPlayers(),
                database.getAllVotes(),
            ) { state, players, votes ->
                Triple(state, players, votes)
            }.collect { (state, playerList, voteList) ->
                _gameState.value = state
                _players.value = playerList
                _votes.value = voteList
            }
        }
    }

    fun updateRoomName(
        name: String,
    ) {
        _createGameState.update {
            it.copy(
                name = name,
            )
        }
    }

    fun startServer() {
        screenModelScope.launch {
            handleIntent(ModeratorCommand.ResetGame(createGameState.value.id))
            val identity = GameIdentity(
                id = createGameState.value.id,
                gameName = createGameState.value.name,
                moderatorName = hostPlayer.value?.name ?: error("Player is not present"),
                moderatorAvatar = hostPlayer.value?.avatar ?: error("Player is not present"),
                moderatorAvatarBackground = hostPlayer.value?.background
                    ?: error("Player is not present"),
            )

            server.start(identity)
            hostPlayer.value?.let {
                handleIntent(
                    ModeratorCommand.CreateGame(
                        createGameState.value.id,
                        createGameState.value,
                        it,
                    ),
                )
            }
                ?: error("Player is not present")

            observeDatabase(identity.id)
        }
    }

    // Moderator actions
    suspend fun handleIntent(intent: ModeratorCommand) {
        val currentGameId = gameState.value?.id ?: createGameState.value.id
        server.handleModeratorCommand(currentGameId, intent)
    }

    override fun onDispose() {
        super.onDispose()
        dbObserveJob?.cancel()
        database.clearPlayers()
        database.clearGameState()
        database.clearVotes()
        screenModelScope.launch(Dispatchers.IO) {
            val targetGameId = gameState.value?.id ?: createGameState.value.id
            handleIntent(ModeratorCommand.ResetGame(targetGameId))
            server.stop()
        }
    }
}
