/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.gameplay

import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

suspend fun validateIntent(
    db: LocalDatabase,
    gameId: String,
    intent: PlayerIntent,
): Boolean {
    val players = db.getAllPlayers().firstOrNull() ?: return false
    val gameState = db.getGameState(gameId).firstOrNull() ?: return false
    val votes = db.getAllVotes().firstOrNull() ?: return false
    val phase = gameState.phase
    val accused = gameState.accusedPlayer
    val lastSaved = gameState.lastSavedPlayerId

    // Handle Join separately — player may not exist in DB yet
    if (intent is PlayerIntent.Join) {
        return phase == GamePhase.LOBBY &&
            players.count { it.role == Role.MODERATOR } == 1 &&
            players.all { it.role == null || it.role == Role.MODERATOR }
    }

    // For all other intents, fetch the player
    val player = db.getPlayerById(intent.playerId).firstOrNull() ?: return false
    val role = player.role

    return when (intent) {
        is PlayerIntent.Kill ->
            player.isAlive &&
                role == Role.GONDI &&
                phase == GamePhase.SLEEP &&
                intent.playerId != intent.targetId &&
                role.canActInSleep

        is PlayerIntent.Save ->
            player.isAlive &&
                role == Role.DOCTOR &&
                phase == GamePhase.SLEEP &&
                lastSaved != intent.targetId &&
                role.canActInSleep

        is PlayerIntent.Investigate ->
            player.isAlive &&
                role == Role.DETECTIVE &&
                phase == GamePhase.SLEEP &&
                role.canActInSleep

        is PlayerIntent.Accuse ->
            player.isAlive &&
                role?.canAccuse == true &&
                role.canVote &&
                phase == GamePhase.TOWN_HALL &&
                player.id != intent.targetId &&
                intent.playerId == player.id

        is PlayerIntent.Second ->
            player.isAlive &&
                role?.canAccuse == true &&
                role.canVote &&
                phase == GamePhase.TOWN_HALL &&
                accused?.targetId == intent.targetId &&
                intent.playerId != intent.targetId &&
                accused.playerId != intent.playerId

        is PlayerIntent.Vote ->
            player.isAlive &&
                role?.canVote == true &&
                gameState.accusedPlayer?.targetId == intent.vote.targetId &&
                player.id != intent.vote.targetId &&
                phase == GamePhase.COURT &&
                intent.vote.voterId == player.id &&
                votes.none { it.voterId == intent.vote.voterId } &&
                votes.count { it.targetId == intent.vote.targetId } < 2

        else -> false
    }
}

val PlayerIntent.validationMessage
    get() = when (this) {
        is PlayerIntent.Accuse -> "You are not allowed to accuse this player."
        is PlayerIntent.Investigate -> "You cannot investigate this player at this time."
        is PlayerIntent.Join -> "You cannot join the game at this time."
        is PlayerIntent.Kill -> "You are not allowed to kill this player."
        is PlayerIntent.Save -> "You are not allowed to save this player."
        is PlayerIntent.Second -> "You cannot second this accusation."
        is PlayerIntent.Vote -> "You cannot vote at this time."
    }
