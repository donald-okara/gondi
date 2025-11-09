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
    val player = db.getPlayerById(intent.playerId).firstOrNull() ?: return false
    val gameState = db.getGameState(gameId).firstOrNull() ?: return false
    val accused = gameState.accusedPlayer

    return when (intent) {
        is PlayerIntent.Join -> currentPhase == GamePhase.LOBBY
        is PlayerIntent.Kill ->
            player.isAlive &&
                currentPhase == GamePhase.SLEEP &&
                player.role?.faction == Faction.GONDI &&
                player.role?.canActInSleep == true

        is PlayerIntent.Save ->
            player.isAlive &&
                currentPhase == GamePhase.SLEEP &&
                player.role == Role.DOCTOR &&
                player.role?.canActInSleep == true

        is PlayerIntent.Investigate ->
            player.isAlive &&
                currentPhase == GamePhase.SLEEP &&
                player.role == Role.DETECTIVE &&
                player.role?.canActInSleep == true

        is PlayerIntent.Second ->
            player.isAlive &&
                player.role?.canAccuse == true &&
                player.role?.canVote == true &&
                currentPhase == GamePhase.TOWN_HALL &&
                accused?.playerId != intent.targetId &&
                accused?.playerId == intent.playerId

        is PlayerIntent.Accuse ->
            player.isAlive &&
                player.role?.canAccuse == true &&
                player.role?.canVote == true &&
                currentPhase == GamePhase.TOWN_HALL

        is PlayerIntent.Vote ->
            player.isAlive &&
                player.role?.canVote == true &&
                gameState.accusedPlayer?.targetId == intent.vote.targetId &&
                player.id != intent.vote.targetId &&
                currentPhase == GamePhase.COURT
    }
}
