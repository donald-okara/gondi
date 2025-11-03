package ke.don.domain.gameplay

import ke.don.domain.state.GamePhase

sealed class ModeratorCommand {
    data class AdvancePhase(val to: GamePhase? = null) : ModeratorCommand()
    data class RevealDeaths(val playerIds: List<String>) : ModeratorCommand()
    data class RemovePlayer(val playerId: String) : ModeratorCommand()
    data class AssignRole(val playerId: String, val role: Role) : ModeratorCommand()
    data class DeclareWinner(val winner: Faction) : ModeratorCommand()
    object ResetGame : ModeratorCommand()
}
