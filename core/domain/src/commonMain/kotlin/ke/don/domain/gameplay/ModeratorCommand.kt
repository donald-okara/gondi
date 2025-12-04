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
    abstract val gameId: String

    data class CreateGame(override val gameId: String, val game: GameState, val player: Player) : ModeratorCommand()
    class StartGame(override val gameId: String) : ModeratorCommand()
    class LockJoin(override val gameId: String, val lock: Boolean) : ModeratorCommand()
    data class AdvancePhase(override val gameId: String, val phase: GamePhase) : ModeratorCommand()
    data class RevealDeaths(override val gameId: String) : ModeratorCommand()
    data class ExoneratePlayer(override val gameId: String) : ModeratorCommand()
    data class RemovePlayer(override val gameId: String, val playerId: String) : ModeratorCommand()
    data class AssignRole(override val gameId: String, val playerId: String, val role: Role?) : ModeratorCommand()
    data class AssignRoleBatch(override val gameId: String, val players: List<Player>) : ModeratorCommand()
    data class GameOver(override val gameId: String, val winner: Faction) : ModeratorCommand()
    class ResetGame(override val gameId: String) : ModeratorCommand()
}
