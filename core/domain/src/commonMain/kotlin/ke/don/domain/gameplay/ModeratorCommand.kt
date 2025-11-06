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
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class ModeratorCommand {
    data class CreateGame(val game: GameState, val player: Player) : ModeratorCommand()
    object StartGame: ModeratorCommand()
    data class AdvancePhase(val phase: GamePhase) : ModeratorCommand()
    data class RevealDeaths(val playerIds: List<String>) : ModeratorCommand()
    data class RemovePlayer(val playerId: String) : ModeratorCommand()
    data class AssignRole(val playerId: String, val role: Role) : ModeratorCommand()
    data class AssignRoleBatch(val players: List<Player>) : ModeratorCommand()
    data class DeclareWinner(val winner: Faction) : ModeratorCommand()
    object ResetGame : ModeratorCommand()
}
