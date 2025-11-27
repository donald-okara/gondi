/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
@file:OptIn(ExperimentalTime::class)

package ke.don.game_play.player.useCases

import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.player.model.PlayerState
import ke.don.local.datastore.ProfileStore
import ke.don.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GameClientState(
    private val profileStore: ProfileStore,
) {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    val profileSnapshot = profileStore.profileFlow

    val currentPlayer: Flow<Player?> = combine(players, profileSnapshot) { allPlayers, profile ->
        allPlayers.firstOrNull { it.id == profile?.id }
    }


    val logger = Logger("ClientState")

    @OptIn(ExperimentalTime::class)
    fun handleServerUpdate(json: String) {
        val update = Json.decodeFromString<ServerUpdate>(json)
        when (update) {
            is ServerUpdate.GameStateSnapshot -> _gameState.value = update.state
            is ServerUpdate.PlayersSnapshot -> _players.value = update.players
            is ServerUpdate.VotesSnapshot -> _votes.value = update.votes
            is ServerUpdate.Error -> {
                logger.error("❌ ${update.message}")
                Matcha.error(title = update.message)
            }
            is ServerUpdate.Forbidden -> {
                logger.warn("❌ ${update.message}")
                Matcha.warning(title = update.message)
            }
            is ServerUpdate.Announcement -> {
                logger.info("📣 ${update.message}")
                Matcha.info(title = update.message)
                _playerState.update {
                    it.copy(announcements = it.announcements + (update.message to Clock.System.now()))
                }
            }
            is ServerUpdate.LastPing -> _playerState.update { it.copy(lastPing = update.long) }
        }
    }

    fun updatePlayerState(
        transform: (PlayerState) -> PlayerState,
    ) {
        _playerState.update {
            transform(it)
        }
    }

    fun clearState() {
        // Reset state
        _gameState.update {
            null
        }
        _players.update {
            emptyList()
        }
        _votes.update {
            emptyList()
        }
    }
}
