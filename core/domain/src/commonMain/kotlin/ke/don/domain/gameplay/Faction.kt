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

enum class Faction {
    GONDI,
    VILLAGER,
    NEUTRAL,
    ;

    companion object {

        /**
         * Determines the winner based on remaining players.
         *
         * The winning conditions are:
         * - **VILLAGER win:** All Gondis are eliminated, or only the Accomplice remains among Gondis.
         * - **GONDI win:** All Villagers are eliminated (and at least one non-Accomplice Gondi remains) or if Gondis outnumber villagers.
         * - **Game continues:** Neither of the above conditions is met.
         *
         * @param players The list of players in the game.
         * @return The winning [Faction] ([VILLAGER] or [GONDI]), or `null` if the game should continue.
         */
        fun List<Player>.checkWinner(): Faction? {
            val alive = this.filter { it.isAlive }
            val gondiAlive = alive.filter { it.role?.faction == GONDI }
            val villageAlive = alive.filter { it.role?.faction == VILLAGER }

            return when {
                // If no Gondi remain, villagers win
                gondiAlive.isEmpty() -> VILLAGER

                // If only the accomplice is left in Gondi faction, villagers win
                gondiAlive.size == 1 && gondiAlive.first().role == Role.ACCOMPLICE -> VILLAGER

                // If no Villagers remain and at least one non-Accomplice Gondi survives, Gondis win
                villageAlive.isEmpty() && gondiAlive.any { it.role != Role.ACCOMPLICE } -> GONDI

                // If Gondis strictly outnumber Villagers, Gondis win
                gondiAlive.size > villageAlive.size -> GONDI

                else -> null // Game continues
            }
        }
    }
}
