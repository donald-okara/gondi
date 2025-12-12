/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.gameplay

import ke.don.domain.state.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class PlayerAction
@OptIn(ExperimentalTime::class)
constructor(
    val type: ActionType,
    val round: Long? = null,
    @SerialName("player_id") val playerId: String? = null,
    @SerialName("target_id") val targetId: String? = null,
    @SerialName("time_stamp") val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
)

@Serializable
enum class ActionType {
    KILL,
    SAVE,
    INVESTIGATE,
    ACCUSE,
    SECOND,
    NONE,
}

fun isActingInSleep(player: Player, round: Long): Boolean {
    if (player.role?.canActInSleep != true) return false
    if (!player.isAlive) return false

    val lastAction = player.lastAction
    val actedThisRound = lastAction?.round == round
    val sameTypeAsRole = lastAction?.type == player.role.actionType

    // If they acted this round, allow only if type was different
    return if (actedThisRound) {
        !sameTypeAsRole
    } else {
        true // hasn't acted this round → allowed
    }
}


typealias SelectedPlayer = Pair<String, ActionType>
