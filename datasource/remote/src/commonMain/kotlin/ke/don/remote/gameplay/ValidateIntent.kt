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

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

suspend fun validateIntent(
    db: LocalDatabase,
    gameId: String,
    intent: PlayerIntent,
    currentPhase: GamePhase,
): Boolean {
    val players = db.getAllPlayers().firstOrNull() ?: return false
    val gameState = db.getGameState(gameId).firstOrNull() ?: return false
    val accused = gameState.accusedPlayer

    // Handle Join separately — player may not exist in DB yet
    if (intent is PlayerIntent.Join) {
        return currentPhase == GamePhase.LOBBY && players.none { it.role != null }
    }

    // For all other intents, fetch the player
    val player = db.getPlayerById(intent.playerId).firstOrNull() ?: return false
    val role = player.role

    return when (intent) {
        is PlayerIntent.Kill -> player.isAlive && role?.faction == Faction.GONDI && currentPhase == GamePhase.SLEEP && role.canActInSleep
        is PlayerIntent.Save -> player.isAlive && role == Role.DOCTOR && currentPhase == GamePhase.SLEEP && role.canActInSleep
        is PlayerIntent.Investigate -> player.isAlive && role == Role.DETECTIVE && currentPhase == GamePhase.SLEEP && role.canActInSleep
        is PlayerIntent.Second -> player.isAlive &&
                role?.canAccuse == true && role.canVote && currentPhase == GamePhase.TOWN_HALL && accused?.targetId == intent.targetId && accused.playerId != intent.playerId
        is PlayerIntent.Accuse -> player.isAlive &&
                role?.canAccuse == true && role.canVote && currentPhase == GamePhase.TOWN_HALL
        is PlayerIntent.Vote -> player.isAlive &&
                role?.canVote == true &&
                gameState.accusedPlayer?.targetId == intent.vote.targetId &&
                player.id != intent.vote.targetId &&
                currentPhase == GamePhase.COURT
        else -> false
    }
}
