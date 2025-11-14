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

import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.server.PhaseValidationResult
import ke.don.domain.state.GamePhase
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

suspend fun validateIntent(
    db: LocalDatabase,
    gameId: String,
    intent: PlayerIntent,
): PhaseValidationResult {
    val players = db.getAllPlayers().firstOrNull() ?: return PhaseValidationResult.Error("No players found.")
    val gameState = db.getGameState(gameId).firstOrNull() ?: return PhaseValidationResult.Error("Game state not found.")
    val votes = db.getAllVotes().firstOrNull() ?: return PhaseValidationResult.Error("No votes found.")
    val phase = gameState.phase
    val round = gameState.round
    val accused = gameState.accusedPlayer
    val lastSaved = gameState.lastSavedPlayerId

    // Handle Join separately
    if (intent is PlayerIntent.Join) {
        if (phase != GamePhase.LOBBY) return PhaseValidationResult.Error("Players can only join during the lobby phase.")
        val moderators = players.count { it.role == Role.MODERATOR }
        if (moderators == 0) return PhaseValidationResult.Error("No moderator assigned. The game requires one moderator to start.")
        if (moderators > 1) return PhaseValidationResult.Error("Multiple moderators assigned. Only one moderator is allowed.")
        if (!players.all { it.role == null || it.role == Role.MODERATOR }) {
            return PhaseValidationResult.Error("Some players already have roles. Joining is only allowed before role assignment.")
        }
        return PhaseValidationResult.Success
    }

    // Fetch the player
    val player = db.getPlayerById(intent.playerId).firstOrNull()
        ?: return PhaseValidationResult.Error("Player with ID ${intent.playerId} not found.")
    val role = player.role ?: return PhaseValidationResult.Error("Player has no assigned role.")

    return when (intent) {
        is PlayerIntent.Kill -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot perform actions.")
            role != Role.GONDI -> PhaseValidationResult.Error("Only Gondi players can perform kills.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.KILL
            -> PhaseValidationResult.Error("You have already killed on this round.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("Kills can only occur during the Sleep phase.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You cannot target yourself.")
            !role.canActInSleep -> PhaseValidationResult.Error("${role.name} cannot act in the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Save -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot perform actions.")
            role != Role.DOCTOR -> PhaseValidationResult.Error("Only the Doctor can save players.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.SAVE
            -> PhaseValidationResult.Error("You have saved someone this round.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("Saves can only happen during the Sleep phase.")
            lastSaved == intent.targetId -> PhaseValidationResult.Error("You cannot save the same player twice in a row.")
            !role.canActInSleep -> PhaseValidationResult.Error("${role.name} cannot act in the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Investigate -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot perform actions.")
            role != Role.DETECTIVE -> PhaseValidationResult.Error("Only the Detective can investigate players.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("Investigations can only occur during the Sleep phase.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.INVESTIGATE
            -> PhaseValidationResult.Error("You have already investigated this round.")
            player.knownIdentities.any { it.playerId == intent.targetId } -> PhaseValidationResult.Error("You already investigated this player.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You cannot investigate yourself.")
            players.none { it.id == intent.targetId && it.isAlive } -> PhaseValidationResult.Error("Target player is either dead or not found.")
            !role.canActInSleep -> PhaseValidationResult.Error("${role.name} cannot act in the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Accuse -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot accuse.")
            role.canAccuse.not() -> PhaseValidationResult.Error("${role.name} cannot accuse.")
            !role.canVote -> PhaseValidationResult.Error("${role.name} cannot vote or accuse.")
            gameState.accusedPlayer != null -> PhaseValidationResult.Error("An accusation is already in progress.")
            gameState.second != null -> PhaseValidationResult.Error("A second is already in progress.")
            phase != GamePhase.TOWN_HALL -> PhaseValidationResult.Error("Accusations can only happen during the Town Hall phase.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You cannot accuse yourself.")
            intent.playerId != player.id -> PhaseValidationResult.Error("Invalid player ID for accusation.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Second -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot second accusations.")
            role.canAccuse.not() -> PhaseValidationResult.Error("${role.name} cannot second accusations.")
            !role.canVote -> PhaseValidationResult.Error("${role.name} cannot vote or second.")
            phase != GamePhase.TOWN_HALL -> PhaseValidationResult.Error("Seconding can only happen during the Town Hall phase.")
            accused?.targetId != intent.targetId -> PhaseValidationResult.Error("You can only second the currently accused player.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You cannot second yourself.")
            accused.playerId == intent.playerId -> PhaseValidationResult.Error("You cannot second your own accusation.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Vote -> when {
            !player.isAlive -> PhaseValidationResult.Error("Dead players cannot vote.")
            role.canVote.not() -> PhaseValidationResult.Error("${role.name} cannot vote.")
            gameState.accusedPlayer?.targetId != intent.vote.targetId -> PhaseValidationResult.Error("Vote target does not match the current accused player.")
            player.id == intent.vote.targetId -> PhaseValidationResult.Error("You cannot vote for yourself.")
            phase != GamePhase.COURT -> PhaseValidationResult.Error("Voting is only allowed during the Court phase.")
            intent.vote.voterId != player.id -> PhaseValidationResult.Error("Voter ID does not match the current player.")
            votes.any { it.voterId == intent.vote.voterId } -> PhaseValidationResult.Error("You have already voted.")
            votes.count { it.targetId == intent.vote.targetId } >= 2 -> PhaseValidationResult.Error("This player has already received the maximum number of votes.")
            else -> PhaseValidationResult.Success
        }

        else -> PhaseValidationResult.Error("Unsupported player intent type.")
    }
}
