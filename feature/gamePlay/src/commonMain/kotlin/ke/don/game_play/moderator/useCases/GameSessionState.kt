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

package ke.don.game_play.moderator.useCases

import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.local.datastore.ProfileStore
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class GameSessionState(
    private val database: LocalDatabase,
    private val profileStore: ProfileStore,
) {
    val gameState = MutableStateFlow<GameState?>(null)
    val players = MutableStateFlow<List<Player>>(emptyList())
    val votes = MutableStateFlow<List<Vote>>(emptyList())
    val moderatorState = MutableStateFlow(ModeratorState())
    val profileSnapshot = profileStore.profileFlow

    val hostPlayer : Flow<Player?> = combine(players, profileSnapshot) { allPlayers, profile ->
        allPlayers.firstOrNull { it.id == profile?.id }
    }

    private var dbObserveJob: Job? = null

    fun updateModeratorState(transform: (ModeratorState) -> ModeratorState) {
        moderatorState.update {
            transform(moderatorState.value)
        }
    }

    fun observe(gameId: String, scope: CoroutineScope) {
        dbObserveJob?.cancel()

        dbObserveJob = scope.launch {
            combine(
                database.getGameState(gameId),
                database.getAllPlayers(),
                database.getAllVotes(),
            ) { state, players, votes ->
                Triple(state, players, votes)
            }.collect { (state, playerList, voteList) ->
                gameState.value = state
                players.value = playerList
                votes.value = voteList
            }
        }
    }
    fun stopObserving() {
        dbObserveJob?.cancel()

        moderatorState.update { ModeratorState() }
        gameState.update { null }
        players.update { emptyList() }
        votes.update { emptyList() }
    }
}
