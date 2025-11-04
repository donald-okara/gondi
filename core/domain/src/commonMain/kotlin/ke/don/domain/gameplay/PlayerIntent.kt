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

import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class PlayerIntent {
    abstract val playerId: String

    @Serializable
    data class Kill(override val playerId: String, val targetId: String) : PlayerIntent()

    @Serializable
    data class Save(override val playerId: String, val targetId: String) : PlayerIntent()

    @Serializable
    data class Investigate(override val playerId: String, val targetId: String) : PlayerIntent()

    @Serializable
    data class Accuse(override val playerId: String, val targetId: String) : PlayerIntent()

    @Serializable
    data class Second(override val playerId: String, val targetId: String) : PlayerIntent()

    @Serializable
    data class Vote(override val playerId: String, val vote: ke.don.domain.state.Vote) : PlayerIntent()
}
