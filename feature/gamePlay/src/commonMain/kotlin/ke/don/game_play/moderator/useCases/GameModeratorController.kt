package ke.don.game_play.moderator.useCases

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.server.LocalServer
import ke.don.game_play.moderator.model.RoleAssignment
import ke.don.game_play.moderator.useCases.GameSessionState
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
                newGame = state.newGame.copy(name = name)
            )
            println("Updated: ${newState.newGame.name}")
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
                    cause = "Detective/Accomplice"
                )
            )
        }

        // Only one Detective/Accomplice
        if (detectiveCount > 1 || accompliceCount > 1) {
            return Result.error(
                LocalError(
                    message = "Only one Detective or Accomplice allowed",
                    cause = "Detective/Accomplice"
                )
            )
        }

        // Hard rules
        if (doctorCount != 1) {
            return Result.error(
                LocalError(
                    message = "There must be exactly 1 Doctor",
                    cause = "Doctor"
                )
            )
        }

        if (gondiCount != 2) {
            return Result.error(
                LocalError(
                    message = "There must be exactly 2 Gondis",
                    cause = "Gondi"
                )
            )
        }

        // Minimum
        if (totalPlayers < 4) {
            return Result.error(
                LocalError(
                    message = "At least 4 players are required",
                    cause = "PlayerCount"
                )
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

    suspend fun assignRoles(): Result<Unit, LocalError> {
        try {
            val moderatorState = session.moderatorState.value
            val assignments = moderatorState.assignment
            val players =
                session.players.value.filterNot { it.role == Role.MODERATOR && !it.isAlive }


            // Refuse to start if players < 5
            if (players.size < 5) {
                val message = "Not enough players to start the game: minimum 5 required, have ${players.size}"
                session.updateModeratorState { state ->
                    state.copy(assignmentsStatus = ResultStatus.Error(message))
                }
                return Result.Error(LocalError(message, "PlayerCount"))
            }

            // Adjust assignments if players < 10: force detective and accomplice to 0
            val adjustedAssignments = if (players.size < 10) {
                assignments.map { (role, count) ->
                    if (role == Role.DETECTIVE || role == Role.ACCOMPLICE) role to 0 else role to count
                }
            } else {
                assignments
            }

            val totalAdjusted = adjustedAssignments.sumOf { it.second }

            // Build role pool
            val rolePool = buildList(players.size) {
                for ((role, count) in adjustedAssignments) {
                    repeat(count) { add(role) }
                }
                if (players.size > totalAdjusted) {
                    repeat(players.size - totalAdjusted) { add(Role.VILLAGER) }
                }
            }.shuffled()

            // Assign roles randomly
            val assigned = players.shuffled().zip(rolePool).map { (player, role) ->
                player.copy(role = role)
            }

            val gameId = moderatorState.newGame.id

            // Update game on server
            server.handleModeratorCommand(
                gameId,
                ModeratorCommand.AssignRoleBatch(gameId, assigned)
            )

            session.updateModeratorState { it.copy(assignmentsStatus = ResultStatus.Success(Unit)) }

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(
                LocalError(
                    message = e.message ?: "Unknown error",
                    cause = e.cause.toString()
                )
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

