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
    val players = db.getAllPlayers().firstOrNull() ?: return PhaseValidationResult.Error("No players are in the game yet.")
    val gameState = db.getGameState(gameId).firstOrNull() ?: return PhaseValidationResult.Error("Game state could not be found.")
    val votes = db.getAllVotes().firstOrNull() ?: return PhaseValidationResult.Error("There are no votes yet.")
    val phase = gameState.phase
    val round = gameState.round
    val accused = gameState.accusedPlayer
    val lastSaved = gameState.lastSavedPlayerId

    if (intent is PlayerIntent.Join) {
        return when {
            phase != GamePhase.LOBBY ->
                PhaseValidationResult.Error("You can only join while the game is in the lobby.")
            players.count { it.role == Role.MODERATOR } == 0 ->
                PhaseValidationResult.Error("You cannot join — the game needs a moderator first.")
            players.count { it.role == Role.MODERATOR } > 1 ->
                PhaseValidationResult.Error("There are too many moderators. Only one is allowed.")
            !players.all { it.role == null || it.role == Role.MODERATOR } ->
                PhaseValidationResult.Error("Some players already have roles. Joining is only allowed before roles are assigned.")
            players.any { it.id == intent.player.id && it.isAlive.not() } ->
                PhaseValidationResult.Error("You can't join because you're already eliminated; you can stay and spectate")
            players.any { it.id == intent.player.id && it.isAlive } ->
                PhaseValidationResult.Error("You're already in the game!")
            gameState.lockJoin ->
                PhaseValidationResult.Error("The game is locked for joining right now.")
            else -> PhaseValidationResult.Success
        }
    }

    if (intent is PlayerIntent.Leave) {
        return PhaseValidationResult.Success
    }

    val player = db.getPlayerById(intent.playerId).firstOrNull()
        ?: return PhaseValidationResult.Error("We couldn't find your player data.")
    val role = player.role ?: return PhaseValidationResult.Error("You don't have a role assigned yet.")

    return when (intent) {
        is PlayerIntent.Kill -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't perform actions because you are eliminated.")
            role != Role.GONDI -> PhaseValidationResult.Error("Only Gondi players can perform kills.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.KILL ->
                PhaseValidationResult.Error("You've already made a kill this round.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("You can only perform kills during the Sleep phase.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You can't target yourself.")
            !role.canActInSleep -> PhaseValidationResult.Error("Your role cannot act during the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Save -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't perform actions because you are eliminated.")
            role != Role.DOCTOR -> PhaseValidationResult.Error("Only the Doctor can save players.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.SAVE ->
                PhaseValidationResult.Error("You've already saved someone this round.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("You can only save during the Sleep phase.")
            lastSaved == intent.targetId -> PhaseValidationResult.Error("You just saved this player. Pick someone else.")
            !role.canActInSleep -> PhaseValidationResult.Error("Your role cannot act during the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Investigate -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't investigate because you are eliminated.")
            role != Role.DETECTIVE -> PhaseValidationResult.Error("Only the Detective can investigate.")
            phase != GamePhase.SLEEP -> PhaseValidationResult.Error("Investigations are only allowed during the Sleep phase.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.INVESTIGATE ->
                PhaseValidationResult.Error("You've already investigated this round.")
            player.knownIdentities.any { it.playerId == intent.targetId } ->
                PhaseValidationResult.Error("You already investigated this player.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You can't investigate yourself.")
            players.none { it.id == intent.targetId && it.isAlive } ->
                PhaseValidationResult.Error("That player is either dead or not found.")
            !role.canActInSleep -> PhaseValidationResult.Error("Your role cannot act during the Sleep phase.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Accuse -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't accuse because you are eliminated.")
            role.canAccuse.not() -> PhaseValidationResult.Error("Your role cannot make accusations.")
            !role.canVote -> PhaseValidationResult.Error("Your role cannot vote or accuse.")
            gameState.accusedPlayer != null -> PhaseValidationResult.Error("Someone is already accused.")
            gameState.second != null -> PhaseValidationResult.Error("A second is already in progress.")
            phase != GamePhase.TOWN_HALL -> PhaseValidationResult.Error("You can only accuse during the Town Hall phase.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You can't accuse yourself.")
            intent.playerId != player.id -> PhaseValidationResult.Error("This is not your turn to accuse.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Second -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't second an accusation because you are eliminated.")
            role.canAccuse.not() -> PhaseValidationResult.Error("Your role cannot second accusations.")
            !role.canVote -> PhaseValidationResult.Error("Your role cannot vote or second.")
            phase != GamePhase.TOWN_HALL -> PhaseValidationResult.Error("You can only second during the Town Hall phase.")
            accused?.targetId != intent.targetId -> PhaseValidationResult.Error("You can only second the current accused player.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You can't second yourself.")
            accused.playerId == intent.playerId -> PhaseValidationResult.Error("You can't second your own accusation.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Vote -> when {
            !player.isAlive -> PhaseValidationResult.Error("You can't vote because you are eliminated.")
            role.canVote.not() -> PhaseValidationResult.Error("Your role cannot vote.")
            gameState.accusedPlayer?.targetId != intent.vote.targetId ->
                PhaseValidationResult.Error("The vote target doesn't match the current accused player.")
            player.id == intent.vote.targetId -> PhaseValidationResult.Error("You can't vote for yourself.")
            phase != GamePhase.COURT -> PhaseValidationResult.Error("Voting only happens during the Court phase.")
            intent.vote.voterId != player.id -> PhaseValidationResult.Error("That's not your voter ID.")
            votes.any { it.voterId == intent.vote.voterId } -> PhaseValidationResult.Error("You already voted.")
            votes.count { it.targetId == intent.vote.targetId } >= 2 -> PhaseValidationResult.Error("This player has already reached the max votes.")
            else -> PhaseValidationResult.Success
        }

        else -> PhaseValidationResult.Error("We can't handle this action type yet.")
    }
}
