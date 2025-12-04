/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.state

import kotlinx.serialization.Serializable

@Serializable
enum class GamePhase {
    LOBBY,
    SLEEP,
    TOWN_HALL,
    COURT,
    GAME_OVER,
}

val GamePhase.nextPhase: GamePhase
    get() = when(this) {
        GamePhase.LOBBY -> GamePhase.SLEEP
        GamePhase.SLEEP -> GamePhase.TOWN_HALL
        GamePhase.TOWN_HALL -> GamePhase.COURT
        GamePhase.COURT -> GamePhase.SLEEP
        GamePhase.GAME_OVER -> GamePhase.LOBBY
    }
