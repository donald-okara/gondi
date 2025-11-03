package ke.don.domain.gameplay

import ke.don.domain.state.Player

enum class Faction {
    GONDI,
    VILLAGER,
    NEUTRAL;

    companion object {

        /**
         * Determines the winner based on remaining players.
         *
         * The winning conditions are:
         * - **VILLAGER win:** All Gondis are eliminated, and there is more than one Villager left.
         * - **GONDI win:** The number of Gondis is equal to or greater than the number of Villagers.
         * - **Game continues:** Neither of the above conditions is met.
         *
         * @param players The list of players in the game.
         * @return The winning [Faction] ([VILLAGER] or [GONDI]), or `null` if the game should continue.
         */
        fun checkWinner(players: List<Player>): Faction? {
            val alive = players.filter { it.isAlive }
            val gondiAlive = alive.filter { it.role.faction == Faction.GONDI }
            val villageAlive = alive.filter { it.role.faction == Faction.VILLAGER }

            return when {
                // If no Gondi remain, villagers win
                gondiAlive.isEmpty() -> Faction.VILLAGER

                // If no Villagers remain, Gondi win
                villageAlive.isEmpty() -> Faction.GONDI

                // If only the accomplice is left in Gondi faction, villagers win
                gondiAlive.size == 1 && gondiAlive.first().role == Role.ACCOMPLICE -> Faction.VILLAGER

                else -> null // Game continues
            }
        }
    }
}
