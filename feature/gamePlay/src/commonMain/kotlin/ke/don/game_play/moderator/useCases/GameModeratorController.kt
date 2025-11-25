/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.useCases

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.server.LocalServer
import ke.don.game_play.moderator.model.RoleAssignment
import ke.don.utils.result.LocalError
import ke.don.utils.result.Result
import ke.don.utils.result.ResultStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameModeratorController(
    private val session: GameSessionState,
    private val server: LocalServer,
) {
    private val pairedRoles = setOf(Role.DETECTIVE, Role.ACCOMPLICE)

    fun updateRoomName(name: String) {
        session.updateModeratorState { state ->
            val newState = state.copy(
                newGame = state.newGame.copy(name = name),
            )
            newState
        }
    }

    fun validateAssignments(): Result<Unit, LocalError> {
        val moderatorState = session.moderatorState.value
        val assignments = moderatorState.assignment

        val totalPlayers = assignments
            .filterNot { it.first == Role.MODERATOR }
            .sumOf { it.second }

        val doctorCount = assignments.firstOrNull { it.first == Role.DOCTOR }?.second ?: 0
        val gondiCount = assignments.firstOrNull { it.first == Role.GONDI }?.second ?: 0
        val detectiveCount = assignments.firstOrNull { it.first == Role.DETECTIVE }?.second ?: 0
        val accompliceCount = assignments.firstOrNull { it.first == Role.ACCOMPLICE }?.second ?: 0

        // <10 players rule
        if (totalPlayers < 10 && (detectiveCount > 0 || accompliceCount > 0)) {
            return Result.error(
                LocalError(
                    message = "Detective and Accomplice cannot exist in a game with less than 10 players",
                    cause = "Detective/Accomplice",
                ),
            )
        }

        // Only one Detective/Accomplice
        if (detectiveCount > 1 || accompliceCount > 1) {
            return Result.error(
                LocalError(
                    message = "Only one Detective or Accomplice allowed",
                    cause = "Detective/Accomplice",
                ),
            )
        }

        // Hard rules
        if (doctorCount != 1) {
            return Result.error(
                LocalError(
                    message = "There must be exactly 1 Doctor",
                    cause = "Doctor",
                ),
            )
        }

        if (gondiCount != 2) {
            return Result.error(
                LocalError(
                    message = "There must be exactly 2 Gondis",
                    cause = "Gondi",
                ),
            )
        }

        // Minimum
        if (totalPlayers < PLAYER_LOWER_LIMIT) {
            return Result.error(
                LocalError(
                    message = "At least $PLAYER_LOWER_LIMIT players are required",
                    cause = "PlayerCount",
                ),
            )
        }

        return Result.success(Unit)
    }

    fun updateAssignment(assignment: RoleAssignment) {
        session.updateModeratorState { state ->
            val newAssignments = state.assignment.map { existing ->
                when {
                    // Detective/Accomplice always match
                    assignment.first in pairedRoles && existing.first in pairedRoles ->
                        existing.copy(second = assignment.second)

                    // Any normal assignment
                    existing.first == assignment.first ->
                        assignment

                    else -> existing
                }
            }
            state.copy(assignment = newAssignments)
        }
    }

    fun selectPlayer(playerId: String? = null) {
        session.updateModeratorState { state ->
            state.copy(selectedPlayerId = playerId)
        }
    }

    suspend fun assignRoles(): Result<Unit, LocalError> {
        try {
            val moderatorState = session.moderatorState.value
            val assignments = moderatorState.assignment

            // All players except dead moderator
            val totalPlayers =
                session.players.value.filterNot { it.role == Role.MODERATOR && !it.isAlive }

            // Players who need random assignment
            val assignPlayers = totalPlayers.filter { it.role == null }

            // Enforce minimum player requirement
            if (totalPlayers.size < PLAYER_LOWER_LIMIT) {
                val message =
                    "Not enough players to start the game: minimum 5 required, have ${totalPlayers.size}"
                session.updateModeratorState { state ->
                    state.copy(assignmentsStatus = ResultStatus.Error(message))
                }
                return Result.Error(LocalError(message, "PlayerCount"))
            }

            // ----------- NEW LOGIC: Deduct slots for pre-assigned roles -----------
            val alreadyAssignedCounts = totalPlayers
                .filter { it.role != null }
                .groupingBy { it.role!! }
                .eachCount()

            val adjustedForExisting = assignments.map { (role, count) ->
                val used = alreadyAssignedCounts[role] ?: 0
                role to (count - used).coerceAtLeast(0)
            }
            // -----------------------------------------------------------------------

            // Adjust assignments if players < 10: force detective + accomplice = 0
            val adjustedAssignments =
                if (totalPlayers.size < PLAYER_DET_LIMIT) {
                    adjustedForExisting.map { (role, count) ->
                        if (role == Role.DETECTIVE || role == Role.ACCOMPLICE) {
                            role to 0
                        } else {
                            role to count
                        }
                    }
                } else {
                    adjustedForExisting
                }

            val totalAdjusted = adjustedAssignments.sumOf { it.second }

            // Build final role pool (remaining slots + villagers)
            val rolePool = buildList(assignPlayers.size) {
                for ((role, count) in adjustedAssignments) {
                    repeat(count) { add(role) }
                }

                // Fill remaining slots with Villagers
                val poolSize = size
                if (assignPlayers.size > poolSize) {
                    repeat(assignPlayers.size - poolSize) { add(Role.VILLAGER) }
                }
            }.shuffled()

            // Assign roles ONLY to players without roles
            val newlyAssigned = assignPlayers.shuffled().zip(rolePool).map { (player, role) ->
                player.copy(role = role)
            }

            // Combine pre-assigned players + new assignments
            val finalAssignments = totalPlayers.map { p ->
                newlyAssigned.find { it.id == p.id } ?: p
            }

            val gameId = moderatorState.newGame.id

            // Send batch update to server
            server.handleModeratorCommand(
                gameId,
                ModeratorCommand.AssignRoleBatch(gameId, finalAssignments),
            )

            session.updateModeratorState {
                it.copy(assignmentsStatus = ResultStatus.Success(Unit))
            }

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(
                LocalError(
                    message = e.message ?: "Unknown error",
                    cause = e.cause.toString(),
                ),
            )
        }
    }

    fun handleCommand(cmd: ModeratorCommand, scope: CoroutineScope) {
        scope.launch {
            val currentGameId = session.gameState.value?.id ?: session.moderatorState.value.newGame.id

            server.handleModeratorCommand(currentGameId, cmd)
        }
    }
}

const val PLAYER_LOWER_LIMIT = 5
const val PLAYER_DET_LIMIT = 8
