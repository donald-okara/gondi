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
    fun getAllGameState(): Flow<List<GameState>> = stateQueries.getAllGAmeState()
        .asFlow()
        .map { it.executeAsList().map { state -> state.toGameState } }

    fun getGameState(id: String): Flow<GameState?> = stateQueries.getGameState(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toGameState }

    fun insertOrReplaceGameState(gameState: GameState) = stateQueries.insertOrReplaceGameState(
        gameState.toGameState.id,
        gameState.toGameState.phase,
        gameState.toGameState.round,
        gameState.toGameState.pending_kills,
        gameState.toGameState.last_saved_player_id,
        gameState.toGameState.accused_player_id,
        gameState.toGameState.reveal_eliminated_player,
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
        player.id,
        name = player.name,
        role = player.role?.let { roleAdapter.encode(it) },
        is_alive = booleanAdapter.encode(player.isAlive),
        known_identities = knownIdentitiesAdapter.encode(player.knownIdentities),
        last_action = player.lastAction?.let { playerActionAdapter.encode(it) },
        avatar = player.avatar?.let { avatarAdapter.encode(it) },
        background = player.background.let { backgroundAdapter.encode(it) },
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

    fun insertOrReplaceVote(voterId: String, targetId: String, isGuilty: Boolean) = votesQueries.insertOrReplaceVote(voterId, targetId, booleanAdapter.encode(isGuilty))

    fun clearVotes() = votesQueries.clearVotes()
}
