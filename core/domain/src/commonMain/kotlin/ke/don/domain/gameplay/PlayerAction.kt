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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class PlayerAction
@OptIn(ExperimentalTime::class) constructor(
    val type: ActionType,
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
    VOTE_GUILTY,
    VOTE_INNOCENT,
    NONE,
}
