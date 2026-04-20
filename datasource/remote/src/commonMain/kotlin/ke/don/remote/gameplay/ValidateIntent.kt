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
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
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

    if (intent is PlayerIntent.Leave) {
        return PhaseValidationResult.Success
    }

    return when (phase) {
        GamePhase.LOBBY -> validateLobbyPhase(intent, gameState, players)
        GamePhase.SLEEP -> validateSleepPhase(db, intent, gameState, players)
        GamePhase.TOWN_HALL -> validateTownHallPhase(db, intent, gameState)
        GamePhase.COURT -> validateCourtPhase(db, intent, gameState, votes)
        else -> PhaseValidationResult.Error("Actions are not allowed in the current phase: $phase")
    }
}

private fun validateLobbyPhase(
    intent: PlayerIntent,
    gameState: GameState,
    players: List<Player>
): PhaseValidationResult {
    if (intent !is PlayerIntent.Join) {
        return PhaseValidationResult.Error("Only join actions are allowed in the lobby.")
    }

    return when {
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

private suspend fun validateSleepPhase(
    db: LocalDatabase,
    intent: PlayerIntent,
    gameState: GameState,
    players: List<Player>
): PhaseValidationResult {
    val player = db.getPlayerById(intent.playerId).firstOrNull()
        ?: return PhaseValidationResult.Error("We couldn't find your player data.")
    val role = player.role ?: return PhaseValidationResult.Error("You don't have a role assigned yet.")
    val round = gameState.round
    val lastSaved = gameState.lastSavedPlayerId

    if (!player.isAlive) return PhaseValidationResult.Error("You can't perform actions because you are eliminated.")
    if (!role.canActInSleep) return PhaseValidationResult.Error("Your role cannot act during the Sleep phase.")

    return when (intent) {
        is PlayerIntent.Kill -> when {
            role != Role.GONDI -> PhaseValidationResult.Error("Only Gondi players can perform kills.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.KILL ->
                PhaseValidationResult.Error("You've already made a kill this round.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You can't target yourself.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Save -> when {
            role != Role.DOCTOR -> PhaseValidationResult.Error("Only the Doctor can save players.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.SAVE ->
                PhaseValidationResult.Error("You've already saved someone this round.")
            lastSaved == intent.targetId -> PhaseValidationResult.Error("You just saved this player. Pick someone else.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Investigate -> when {
            role != Role.DETECTIVE -> PhaseValidationResult.Error("Only the Detective can investigate.")
            player.lastAction?.round == round && player.lastAction?.type == ActionType.INVESTIGATE ->
                PhaseValidationResult.Error("You've already investigated this round.")
            player.knownIdentities.any { it.playerId == intent.targetId } ->
                PhaseValidationResult.Error("You already investigated this player.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You can't investigate yourself.")
            players.none { it.id == intent.targetId && it.isAlive } ->
                PhaseValidationResult.Error("That player is either dead or not found.")
            else -> PhaseValidationResult.Success
        }

        else -> PhaseValidationResult.Error("That action is not allowed during the Sleep phase.")
    }
}

private suspend fun validateTownHallPhase(
    db: LocalDatabase,
    intent: PlayerIntent,
    gameState: GameState
): PhaseValidationResult {
    val player = db.getPlayerById(intent.playerId).firstOrNull()
        ?: return PhaseValidationResult.Error("We couldn't find your player data.")
    val role = player.role ?: return PhaseValidationResult.Error("You don't have a role assigned yet.")
    val accused = gameState.accusedPlayer

    if (!player.isAlive) return PhaseValidationResult.Error("You can't act because you are eliminated.")

    return when (intent) {
        is PlayerIntent.Accuse -> when {
            role.canAccuse.not() || !role.canVote -> PhaseValidationResult.Error("Your role cannot make accusations.")
            gameState.accusedPlayer != null -> PhaseValidationResult.Error("Someone is already accused.")
            gameState.second != null -> PhaseValidationResult.Error("A second is already in progress.")
            player.id == intent.targetId -> PhaseValidationResult.Error("You can't accuse yourself.")
            else -> PhaseValidationResult.Success
        }

        is PlayerIntent.Second -> when {
            role.canAccuse.not() || !role.canVote -> PhaseValidationResult.Error("Your role cannot second accusations.")
            accused?.targetId != intent.targetId -> PhaseValidationResult.Error("You can only second the current accused player.")
            intent.playerId == intent.targetId -> PhaseValidationResult.Error("You can't second yourself.")
            accused.playerId == intent.playerId -> PhaseValidationResult.Error("You can't second your own accusation.")
            else -> PhaseValidationResult.Success
        }

        else -> PhaseValidationResult.Error("That action is not allowed during the Town Hall phase.")
    }
}

private suspend fun validateCourtPhase(
    db: LocalDatabase,
    intent: PlayerIntent,
    gameState: GameState,
    votes: List<Vote>
): PhaseValidationResult {
    val player = db.getPlayerById(intent.playerId).firstOrNull()
        ?: return PhaseValidationResult.Error("We couldn't find your player data.")
    val role = player.role ?: return PhaseValidationResult.Error("You don't have a role assigned yet.")

    if (!player.isAlive) return PhaseValidationResult.Error("You can't vote because you are eliminated.")

    return when (intent) {
        is PlayerIntent.Vote -> when {
            role.canVote.not() -> PhaseValidationResult.Error("Your role cannot vote.")
            gameState.accusedPlayer?.targetId != intent.vote.targetId ->
                PhaseValidationResult.Error("You are attempting to vote for a player that is currently not accused.")
            player.id == intent.vote.targetId -> PhaseValidationResult.Error("You can't vote for yourself.")
            votes.any { it.voterId == intent.vote.voterId } -> PhaseValidationResult.Error("You already voted.")
            else -> PhaseValidationResult.Success
        }

        else -> PhaseValidationResult.Error("Only voting is allowed during the Court phase.")
    }
}
