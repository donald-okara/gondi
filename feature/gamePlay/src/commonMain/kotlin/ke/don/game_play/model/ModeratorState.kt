package ke.don.game_play.model

import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.utils.result.ResultStatus

data class ModeratorState(
    val newGame: GameState = GameState(),
    val assignment: List<RoleAssignment> = emptyList(),
    val assignmentsStatus: ResultStatus<Unit> = ResultStatus.Idle,
)

typealias RoleAssignment = Pair<Role, Int>