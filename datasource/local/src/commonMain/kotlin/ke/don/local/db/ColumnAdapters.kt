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

import app.cash.sqldelight.ColumnAdapter
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

val roleAdapter = object : ColumnAdapter<Role, String> {
    override fun decode(databaseValue: String) = Role.valueOf(databaseValue)
    override fun encode(value: Role) = value.name
}

val avatarAdapter = object : ColumnAdapter<Avatar, String> {
    override fun decode(databaseValue: String) = Avatar.valueOf(databaseValue)
    override fun encode(value: Avatar) = value.name
}

val backgroundAdapter = object : ColumnAdapter<AvatarBackground, String> {
    override fun decode(databaseValue: String) = AvatarBackground.valueOf(databaseValue)
    override fun encode(value: AvatarBackground) = value.name
}

val playerActionAdapter = object : ColumnAdapter<PlayerAction, String> {
    override fun decode(databaseValue: String): PlayerAction =
        json.decodeFromString(databaseValue)
    override fun encode(value: PlayerAction): String =
        json.encodeToString(value)
}

val knownIdentitiesAdapter = object : ColumnAdapter<Map<String, Role?>, String> {
    override fun decode(databaseValue: String): Map<String, Role?> =
        json.decodeFromString(databaseValue)
    override fun encode(value: Map<String, Role?>): String =
        json.encodeToString(value)
}

val phaseAdapter = object : ColumnAdapter<GamePhase, String> {
    override fun decode(databaseValue: String): GamePhase = GamePhase.valueOf(databaseValue)

    override fun encode(value: GamePhase): String = value.name
}

val booleanAdapter = object : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean = databaseValue == 1L

    override fun encode(value: Boolean): Long = if (value) 1L else 0L
}

val pendingKillsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        return if (databaseValue.isEmpty()) listOf() else databaseValue.split(",")
    }

    override fun encode(value: List<String>): String = value.joinToString(",")
}

val GameStateEntity.toGameState: GameState get() = GameState(
    id = this.id,
    phase = phaseAdapter.decode(this.phase),
    round = this.round,
    pendingKills = pendingKillsAdapter.decode(this.pending_kills ?: ""),
    lastSavedPlayerId = this.last_saved_player_id,
    accusedPlayerId = this.accused_player_id,
    revealEliminatedPlayer = booleanAdapter.decode(this.reveal_eliminated_player),
)

val GameState.toGameState: GameStateEntity get() = GameStateEntity(
    id = this.id,
    phase = phaseAdapter.encode(this.phase),
    round = this.round,
    pending_kills = pendingKillsAdapter.encode(this.pendingKills),
    last_saved_player_id = this.lastSavedPlayerId,
    accused_player_id = this.accusedPlayerId,
    reveal_eliminated_player = booleanAdapter.encode(this.revealEliminatedPlayer),
)

val PlayerEntity.toPlayer: Player get() = Player(
    id = this.id,
    name = this.name,
    avatar = this.avatar?.let { avatarAdapter.decode(it) },
    background = this.background?.let { backgroundAdapter.decode(it) } ?: AvatarBackground.entries.first(),
    role = this.role?.let { roleAdapter.decode(it) },
    isAlive = booleanAdapter.decode(this.is_alive),
    lastAction = this.last_action?.let { playerActionAdapter.decode(it) },
    knownIdentities = knownIdentitiesAdapter.decode(this.known_identities ?: "{}"),
)

val Player.toPlayers: PlayerEntity get() = PlayerEntity(
    id = this.id,
    name = this.name,
    avatar = this.avatar?.let { avatarAdapter.encode(it) },
    background = backgroundAdapter.encode(this.background),
    role = this.role?.let { roleAdapter.encode(it) },
    is_alive = booleanAdapter.encode(this.isAlive),
    known_identities = knownIdentitiesAdapter.encode(this.knownIdentities),
    last_action = this.lastAction?.let { playerActionAdapter.encode(it) },
)

val Vote.toVotes: VoteEntity get() = VoteEntity(
    voter_id = this.voterId,
    target_id = this.targetId,
    is_guilty = booleanAdapter.encode(this.isGuilty),
)

val VoteEntity.toVote: Vote get() = Vote(
    voterId = this.voter_id,
    targetId = this.target_id,
    isGuilty = booleanAdapter.decode(this.is_guilty),
)
