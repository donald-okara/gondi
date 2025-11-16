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
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.KnownIdentity
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

val roleAdapter = object : ColumnAdapter<Role, String> {
    override fun decode(databaseValue: String) = runCatching {
        Role.valueOf(databaseValue)
    }.getOrElse {
        throw IllegalStateException("Invalid role value in database: $databaseValue", it)
    }

    override fun encode(value: Role) = value.name
}

val avatarAdapter = object : ColumnAdapter<Avatar, String> {
    override fun decode(databaseValue: String) = runCatching {
        Avatar.valueOf(databaseValue)
    }.getOrElse {
        throw IllegalStateException("Invalid avatar value in database: $databaseValue", it)
    }
    override fun encode(value: Avatar) = value.name
}

val backgroundAdapter = object : ColumnAdapter<AvatarBackground, String> {
    override fun decode(databaseValue: String) = runCatching {
        AvatarBackground.valueOf(databaseValue)
    }.getOrElse {
        throw IllegalStateException("Invalid background value in database: $databaseValue", it)
    }
    override fun encode(value: AvatarBackground) = value.name
}

val playerActionAdapter = object : ColumnAdapter<PlayerAction, String> {
    override fun decode(databaseValue: String): PlayerAction =
        json.decodeFromString(databaseValue)
    override fun encode(value: PlayerAction): String =
        json.encodeToString(value)
}

val knownIdentitiesAdapter = object : ColumnAdapter<List<KnownIdentity>, String> {
    private val serializer = ListSerializer(KnownIdentity.serializer())
    override fun decode(databaseValue: String): List<KnownIdentity> {
        if (databaseValue.isEmpty()) return emptyList()
        return runCatching {
            json.decodeFromString(serializer, databaseValue)
        }.getOrElse { emptyList() }
    }

    override fun encode(value: List<KnownIdentity>): String =
        json.encodeToString(serializer, value)
}

val factionAdapter = object : ColumnAdapter<Faction, String> {
    override fun decode(databaseValue: String): Faction = runCatching {
        Faction.valueOf(databaseValue)
    }.getOrElse {
        throw IllegalStateException("Invalid faction value in database: $databaseValue", it)
    }

    override fun encode(value: Faction): String {
        return value.name
    }
}

val phaseAdapter = object : ColumnAdapter<GamePhase, String> {
    override fun decode(databaseValue: String): GamePhase = runCatching {
        GamePhase.valueOf(databaseValue)
    }.getOrElse {
        throw IllegalStateException("Invalid phase value in database: $databaseValue", it)
    }
    override fun encode(value: GamePhase): String = value.name
}

val booleanAdapter = object : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean = databaseValue == 1L

    override fun encode(value: Boolean): Long = if (value) 1L else 0L
}

val pendingKillsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        return databaseValue.split(",").filter { it.isNotEmpty() }
    }

    override fun encode(value: List<String>): String = value.joinToString(",")
}

val GameStateEntity.toGameState: GameState get() = GameState(
    id = this.id,
    name = this.name,
    phase = phaseAdapter.decode(this.phase),
    round = this.round,
    pendingKills = pendingKillsAdapter.decode(this.pending_kills ?: ""),
    lastSavedPlayerId = this.last_saved_player_id,
    second = this.second?.let { playerActionAdapter.decode(it) },
    accusedPlayer = this.accused_player?.let { playerActionAdapter.decode(it) },
    revealEliminatedPlayer = booleanAdapter.decode(this.reveal_eliminated_player),
    winners = this.winners?.let { factionAdapter.decode(it) },
    lockJoin = booleanAdapter.decode(this.lock_join)
)

val GameState.toGameStateEntity: GameStateEntity get() = GameStateEntity(
    id = this.id,
    name = this.name,
    phase = phaseAdapter.encode(this.phase),
    round = this.round,
    pending_kills = pendingKillsAdapter.encode(this.pendingKills),
    last_saved_player_id = this.lastSavedPlayerId,
    accused_player = this.accusedPlayer?.let { playerActionAdapter.encode(it) },
    reveal_eliminated_player = booleanAdapter.encode(this.revealEliminatedPlayer),
    second = this.second?.let { playerActionAdapter.encode(it) },
    winners = this.winners?.let { factionAdapter.encode(it) },
    lock_join = booleanAdapter.encode(this.lockJoin)
)

val PlayerEntity.toPlayer: Player get() = Player(
    id = this.id,
    name = this.name,
    avatar = this.avatar?.let { avatarAdapter.decode(it) },
    background = this.background?.let { backgroundAdapter.decode(it) } ?: AvatarBackground.entries.first(),
    role = this.role?.let { roleAdapter.decode(it) },
    isAlive = booleanAdapter.decode(this.is_alive),
    lastAction = this.last_action?.let { playerActionAdapter.decode(it) },
    timeOfDeath = this.time_of_death,
    knownIdentities = this.known_identities?.let { knownIdentitiesAdapter.decode(it) } ?: emptyList(),
)

val Player.toPlayerEntity: PlayerEntity get() = PlayerEntity(
    id = this.id,
    name = this.name,
    avatar = this.avatar?.let { avatarAdapter.encode(it) },
    background = backgroundAdapter.encode(this.background),
    role = this.role?.let { roleAdapter.encode(it) },
    is_alive = booleanAdapter.encode(this.isAlive),
    known_identities = knownIdentitiesAdapter.encode(this.knownIdentities),
    time_of_death = this.timeOfDeath,
    last_action = this.lastAction?.let { playerActionAdapter.encode(it) },
)

val Vote.toVoteEntity: VoteEntity get() = VoteEntity(
    voter_id = this.voterId,
    target_id = this.targetId,
    is_guilty = booleanAdapter.encode(this.isGuilty),
)

val VoteEntity.toVote: Vote get() = Vote(
    voterId = this.voter_id,
    targetId = this.target_id,
    isGuilty = booleanAdapter.decode(this.is_guilty),
)
