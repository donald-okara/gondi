package ke.don.remote.server

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.ModeratorEngine
import ke.don.domain.state.GamePhase
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

class DefaultModeratorEngine(private val db: LocalDatabase) : ModeratorEngine {
    override suspend fun handle(command: ModeratorCommand) {
        val gameId = db.getFirstGameState().firstOrNull()?.id
        val currentRound = db.getFirstGameState().firstOrNull()?.round

        when (command) {
            is ModeratorCommand.AdvancePhase ->
                gameId?.let { db.updatePhase(command.phase, round = (currentRound?.plus(1) ?: 0), id = it) }
            is ModeratorCommand.RemovePlayer ->
                db.updateAliveStatus(false, command.playerId)
            is ModeratorCommand.ResetGame -> {
                db.clearGameState()
                db.clearPlayers()
                db.clearVotes()
            }

            is ModeratorCommand.AssignRole -> db.updatePlayerRole(command.role, command.playerId)
            is ModeratorCommand.DeclareWinner ->
                gameId?.let { db.updatePhase(phase = GamePhase.GAME_OVER, round = 0L, id = it) }
            is ModeratorCommand.RevealDeaths -> gameId?.let { db.toggleRevealFlag(true, it) }
            ModeratorCommand.StartGame ->
                gameId?.let { db.updatePhase(GamePhase.SLEEP, round = 0, id = it) }
        }
    }
}