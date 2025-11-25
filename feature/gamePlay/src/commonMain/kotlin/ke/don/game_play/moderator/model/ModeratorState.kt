/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.model

import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.utils.result.ResultStatus

data class ModeratorState(
    val newGame: GameState = GameState(),
    val showLeaveGame: Boolean = false,
    val assignment: List<RoleAssignment> = Role.entries.map { role ->
        role to when (role) {
            Role.DOCTOR -> 1
            Role.GONDI -> 2
            Role.VILLAGER -> 4
            else -> 0
        }
    },
    val assignmentsStatus: ResultStatus<Unit> = ResultStatus.Idle,
    val createStatus: ResultStatus<Unit> = ResultStatus.Idle,
    val showAssignRoles: Boolean = false,
    val selectedPlayerId: String? = null,
)

typealias RoleAssignment = Pair<Role, Int>
