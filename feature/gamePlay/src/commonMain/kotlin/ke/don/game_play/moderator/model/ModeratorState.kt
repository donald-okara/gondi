package ke.don.game_play.moderator.model

import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.utils.result.ResultStatus

data class ModeratorState(
    val newGame: GameState = GameState(),
    val showLeaveGame: Boolean = false,
    val assignment: List<RoleAssignment> = Role.entries.map { role ->
        role to when(role) {
            Role.DOCTOR -> 1
            Role.GONDI -> 2
            Role.VILLAGER -> 4
            else -> 0
        }
    },
    val assignmentsStatus: ResultStatus<Unit> = ResultStatus.Idle,
    val createStatus: ResultStatus<Unit> = ResultStatus.Idle,
    val showAssignRoles: Boolean = false,
)

typealias RoleAssignment = Pair<Role, Int>