package ke.don.remote.server

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Faction.Companion.checkWinner
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.ModeratorEngine
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.local.db.GameStateEntity
import ke.don.local.db.LocalDatabase
import ke.don.local.db.PlayerEntity
import kotlinx.coroutines.flow.firstOrNull

class DefaultModeratorEngine(
    private val db: LocalDatabase
) : ModeratorEngine {

    override suspend fun handle(command: ModeratorCommand) {
        val game = db.getFirstGameState().firstOrNull()
        val players = db.getAlivePlayers()
        val currentRound = game?.round

        when (command) {
            is ModeratorCommand.CreateGame -> {
                db.transaction {
                    db.insertOrReplaceGameState(command.game)
                    db.insertOrReplacePlayer(command.player)
                }
            }

            is ModeratorCommand.AdvancePhase -> game?.let { currentGame ->
                players.firstOrNull()?.let { handlePhaseAdvance(command, currentGame, it, currentRound) }
            }

            is ModeratorCommand.RemovePlayer -> db.updateAliveStatus(false, command.playerId)

            is ModeratorCommand.ResetGame -> db.transaction {
                db.clearGameState()
                db.clearPlayers()
                db.clearVotes()
            }

            is ModeratorCommand.AssignRole -> db.updatePlayerRole(command.role, command.playerId)
            is ModeratorCommand.AssignRoleBatch -> db.batchUpdatePlayerRole(command.players)

            is ModeratorCommand.DeclareWinner -> game?.let {
                db.updatePhase(GamePhase.GAME_OVER, round = 0L, id = it.id)
            }

            is ModeratorCommand.RevealDeaths -> game?.let {
                db.toggleRevealFlag(true, it.id)
            }

            ModeratorCommand.StartGame -> game?.let {
                db.updatePhase(GamePhase.SLEEP, round = 0L, id = it.id)
            }
        }
    }

    private suspend fun handlePhaseAdvance(
        command: ModeratorCommand.AdvancePhase,
        game: GameState,
        players: List<Player>,
        currentRound: Long?
    ) {
        val gameId = game.id
        val round = currentRound ?: error("Current round cannot be null")

        when (command.phase) {
            GamePhase.TOWN_HALL -> handleTownHallPhase(game, command.phase, round, gameId)
            GamePhase.SLEEP -> handleSleepPhase(game, command.phase, round, players, gameId)
            else -> db.updatePhase(command.phase, round, gameId)
        }
    }

    private fun handleTownHallPhase(
        game: GameState,
        phase: GamePhase,
        round: Long,
        gameId: String
    ) {
        val lastSaved = game.lastSavedPlayerId
        val pendingKills = game.pendingKills

        db.transaction {
            if (!pendingKills.contains(lastSaved)) {
                db.updateAliveStatus(isAlive = false, ids = pendingKills)
            }
            db.updateLastSaved(null)
            db.updatePhase(phase, round, gameId)
        }
    }

    private suspend fun handleSleepPhase(
        game: GameState,
        phase: GamePhase,
        round: Long,
        players: List<Player>,
        gameId: String
    ) {
        val votes = db.getAllVotes().firstOrNull()
        val guiltyVotes = votes?.count { it.isGuilty } ?: 0
        val totalVotes = votes?.size ?: 0
        val isGuilty = guiltyVotes > (totalVotes / 2)

        db.transaction {
            if (isGuilty) {
                game.accusedPlayer?.targetId?.let {
                    db.updateAliveStatus(false, it)
                }
            }

            db.updatePendingKills(emptyList())

            when (players.checkWinner()) {
                Faction.GONDI -> {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.GONDI)
                }

                Faction.VILLAGER -> {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.VILLAGER)
                }

                else -> db.updatePhase(phase, round + 1, gameId)
            }
        }
    }
}
