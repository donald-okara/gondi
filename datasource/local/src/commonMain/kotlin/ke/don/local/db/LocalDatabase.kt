/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.db

import app.cash.sqldelight.coroutines.asFlow
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class LocalDatabase(
    databaseFactory: DatabaseFactory,
) {
    private val database = GondiDatabase(
        databaseFactory.createDriver(),
    )
    private val stateQueries = database.game_stateQueries
    private val playersQueries = database.playersQueries
    private val votesQueries = database.votesQueries

    /**
     * GAME STATE
     */
    fun getAllGameState(): Flow<List<GameState>> = stateQueries.getAllGameState()
        .asFlow()
        .map { it.executeAsList().map { state -> state.toGameState } }

    fun getGameState(id: String): Flow<GameState?> = stateQueries.getGameState(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toGameState }

    fun insertOrReplaceGameState(gameState: GameState) = stateQueries.insertOrReplaceGameState(
        gameState.toGameStateEntity.id,
        gameState.toGameStateEntity.phase,
        gameState.toGameStateEntity.round,
        gameState.toGameStateEntity.pending_kills,
        gameState.toGameStateEntity.last_saved_player_id,
        gameState.toGameStateEntity.accused_player_id,
        gameState.toGameStateEntity.reveal_eliminated_player,
    )

    fun updatePhase(phase: String, round: Long, id: String) = stateQueries.updatePhase(phase, round, id)

    fun toggleRevealFlag(flag: Boolean, id: String) = stateQueries.toggleRevealFlag(booleanAdapter.encode(flag), id)

    fun clearGameState() = stateQueries.clearGameState()

    /**
     * PLAYER
     */

    fun getAllPlayers(): Flow<List<Player>> = playersQueries.getAllPlayers()
        .asFlow()
        .map { it.executeAsList().map { player -> player.toPlayer } }

    fun getAlivePlayers(): Flow<List<Player>> = playersQueries.getAlivePlayers()
        .asFlow()
        .map { it.executeAsList().map { player -> player.toPlayer } }

    fun getPlayerById(id: String): Flow<Player?> = playersQueries.getPlayerById(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toPlayer }

    fun insertOrReplacePlayer(player: Player) = playersQueries.insertOrReplacePlayer(
        player.toPlayerEntity.id,
        name = player.toPlayerEntity.name,
        role = player.toPlayerEntity.role,
        is_alive = player.toPlayerEntity.is_alive,
        known_identities = player.toPlayerEntity.known_identities,
        last_action = player.toPlayerEntity.last_action,
        avatar = player.toPlayerEntity.avatar,
        background = player.toPlayerEntity.background,
    )

    fun updateAliveStatus(isAlive: Boolean, id: String) = playersQueries.updateAliveStatus(booleanAdapter.encode(isAlive), id)

    fun updateLastAction(lastAction: PlayerAction, id: String) = playersQueries.updateLastAction(playerActionAdapter.encode(lastAction), id)

    fun updateKnownIdentities(knownIdentities: Map<String, Role?>, id: String) = playersQueries.updateKnownIdentities(knownIdentitiesAdapter.encode(knownIdentities), id)

    fun clearPlayers() = playersQueries.clearPlayers()

    /**
     * VOTE
     */

    fun getAllVotes(): Flow<List<Vote>> = votesQueries.getAllVotes()
        .asFlow()
        .map { it.executeAsList().map { vote -> vote.toVote } }

    fun insertOrReplaceVote(vote: Vote) = votesQueries.insertOrReplaceVote(
        vote.toVoteEntity.voter_id,
        vote.toVoteEntity.target_id,
        vote.toVoteEntity.is_guilty,
    )

    fun clearVotes() = votesQueries.clearVotes()
}
