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
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.KnownIdentity
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map
import kotlin.time.ExperimentalTime

class LocalDatabase(
    databaseFactory: DatabaseFactory,
) {
    private val database = GondiDatabase(
        databaseFactory.createDriver(),
    )
    private val stateQueries = database.game_stateQueries
    private val playersQueries = database.playersQueries
    private val votesQueries = database.votesQueries

    fun transaction(block: () -> Unit) {
        database.transaction {
            block()
        }
    }

    /**
     * GAME STATE
     */
    fun getAllGameState(): Flow<List<GameState>> = stateQueries.getAllGameState()
        .asFlow()
        .map { it.executeAsList().map { state -> state.toGameState } }

    fun getFirstGameState(): Flow<GameState?> = stateQueries.getFirstGameState()
        .asFlow()
        .map { it.executeAsOneOrNull()?.toGameState }

    fun getGameState(id: String): Flow<GameState?> = stateQueries.getGameState(id)
        .asFlow()
        .map { it.executeAsOneOrNull()?.toGameState }

    fun insertOrReplaceGameState(gameState: GameState) = stateQueries.insertOrReplaceGameState(
        gameState.toGameStateEntity.id,
        gameState.toGameStateEntity.name,
        gameState.toGameStateEntity.phase,
        gameState.toGameStateEntity.round,
        gameState.toGameStateEntity.pending_kills,
        gameState.toGameStateEntity.last_saved_player_id,
        gameState.toGameStateEntity.accused_player,
        gameState.toGameStateEntity.reveal_eliminated_player,
        gameState.toGameStateEntity.lock_join,
        gameState.toGameStateEntity.available_slots,
    )

    fun lockJoin(lock: Boolean, id: String) = stateQueries.lockJoin(booleanAdapter.encode(lock), id)

    fun killAction(playerAction: PlayerAction) = database.transaction {
        val originalPendingKills = stateQueries.getFirstGameState()
            .executeAsOneOrNull()?.toGameState?.pendingKills
        if (originalPendingKills?.contains(playerAction.targetId) == false) {
            playerAction.targetId?.let {
                val newList = originalPendingKills.plus(it)
                stateQueries.updatePendingKills(pendingKillsAdapter.encode(newList))
                playersQueries.updateLastAction(last_action = playerActionAdapter.encode(playerAction), id = playerAction.playerId ?: error("PlayerId cannot be null"))
            }
        }
    }

    fun saveAction(playerAction: PlayerAction, gameId: String) = database.transaction {
        stateQueries.updateLastSaved(lastSavedPlayer = playerAction.targetId, gameId)
        playersQueries.updateLastAction(
            last_action = playerActionAdapter.encode(playerAction),
            id = playerAction.playerId ?: error("PlayerId cannot be null"),
        )
    }

    fun updateLastSaved(playerId: String?, gameId: String) = stateQueries.updateLastSaved(lastSavedPlayer = playerId, gameId = gameId)
    fun updatePhase(phase: GamePhase, round: Long, id: String) = stateQueries.updatePhase(phaseAdapter.encode(phase), round, id)

    fun toggleRevealFlag(flag: Boolean, id: String) = stateQueries.toggleRevealFlag(booleanAdapter.encode(flag), id)

    fun updateWinners(winners: Faction, gameId: String) = stateQueries.updateWinners(factionAdapter.encode(winners), gameId)

    fun accusePlayer(accusedPlayer: PlayerAction, id: String) = stateQueries.accusePlayer(playerActionAdapter.encode(accusedPlayer), id)

    fun clearAccused(gameId: String) = stateQueries.accusePlayer(null, gameId)

    fun secondPlayer(accusedPlayer: PlayerAction, id: String) = stateQueries.secondPlayer(playerActionAdapter.encode(accusedPlayer), id)

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
        timeOfDeath = player.toPlayerEntity.time_of_death,
    )

    fun updateAliveStatus(isAlive: Boolean, id: String, round: Long) {
        playersQueries.updateAliveStatus(booleanAdapter.encode(isAlive), id)
        playersQueries.updateTimeOfDeath(
            if (isAlive) null else round,
            id,
        )
    }

    @OptIn(ExperimentalTime::class)
    fun updateAliveStatus(isAlive: Boolean, ids: List<String>, round: Long) = database.transaction {
        ids.forEach { id ->
            updateAliveStatus(isAlive, id, round)
        }
    }
    fun updatePendingKills(pendingKills: List<String>) = stateQueries.updatePendingKills(pendingKillsAdapter.encode(pendingKills))
    fun updateLastAction(lastAction: PlayerAction, id: String) = playersQueries.updateLastAction(playerActionAdapter.encode(lastAction), id)

    fun updateKnownIdentities(knownIdentities: List<KnownIdentity>, id: String) = playersQueries.updateKnownIdentities(knownIdentitiesAdapter.encode(knownIdentities), id)

    fun updatePlayerRole(role: Role?, id: String) = playersQueries.updatePlayerRole(role?.let { roleAdapter.encode(it) }, id)

    fun batchUpdatePlayerRole(players: List<Player>) = database.transaction {
        players.forEach { player ->
            playersQueries.updatePlayerRole(player.role?.let { roleAdapter.encode(it) }, player.id)
        }
    }

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
