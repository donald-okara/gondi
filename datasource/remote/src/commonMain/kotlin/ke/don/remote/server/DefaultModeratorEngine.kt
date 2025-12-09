/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.server

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Faction.Companion.checkWinner
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.ModeratorEngine
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class DefaultModeratorEngine(
    private val db: LocalDatabase,
) : ModeratorEngine {

    override suspend fun handle(gameId: String, command: ModeratorCommand) {
        val game = db.getGameState(gameId).firstOrNull()
        val players = db.getAlivePlayers()
        val currentRound = game?.round
        val currentPhase = game?.phase

        when (command) {
            is ModeratorCommand.CreateGame -> {
                db.transaction {
                    db.insertOrReplaceGameState(command.game)
                    db.insertOrReplacePlayer(command.player.copy(role = Role.MODERATOR))
                }
            }

            is ModeratorCommand.LockJoin -> {
                db.lockJoin(command.lock, command.gameId)
            }

            is ModeratorCommand.AssignRole -> db.updatePlayerRole(command.role, command.playerId)

            is ModeratorCommand.AssignRoleBatch -> db.batchUpdatePlayerRole(command.players)

            is ModeratorCommand.AdvancePhase -> game?.let { currentGame ->
                handlePhaseAdvance(command, currentGame, players, currentRound)
            }

            is ModeratorCommand.RemovePlayer -> db.transaction {
                if (currentPhase == GamePhase.LOBBY) db.updatePlayerRole(role = null, id = command.playerId)
                db.updateAliveStatus(false, command.playerId, currentRound ?: error("Current round cannot be null"))
            }

            is ModeratorCommand.ResetGame -> db.transaction {
                db.clearGameState()
                db.clearPlayers()
                db.clearVotes()
            }

            is ModeratorCommand.GameOver -> game?.let {
                db.transaction {
                    db.updatePhase(GamePhase.GAME_OVER, round = 0L, id = it.id)
                    db.updateWinners(command.winner, it.id)
                }
            }

            is ModeratorCommand.RevealDeaths -> game?.let {
                db.toggleRevealFlag(false, it.id)
                db.toggleRevealFlag(true, it.id)
            }

            is ModeratorCommand.ExoneratePlayer -> db.exoneratePlayer(command.gameId)

            is ModeratorCommand.StartGame -> game?.let {
                db.updatePhase(GamePhase.SLEEP, round = 0L, id = it.id)
            }
        }
    }

    private suspend fun handlePhaseAdvance(
        command: ModeratorCommand.AdvancePhase,
        game: GameState,
        players: Flow<List<Player>>,
        currentRound: Long?,
    ) {
        val gameId = game.id
        val round = currentRound ?: error("Current round cannot be null")

        when (command.phase) {
            GamePhase.TOWN_HALL -> handleTownHallPhase(game,  round, gameId)
            GamePhase.SLEEP -> handleSleepPhase(game, round, players, gameId)
            GamePhase.LOBBY -> handleLobbyPhase( round, gameId, players)
            else -> db.updatePhase(command.phase, round, gameId)
        }
    }

    private suspend fun handleSleepPhase(
        game: GameState,
        round: Long,
        players: Flow<List<Player>>,
        gameId: String,
    ) {
        val votes = db.getAllVotes().firstOrNull()

        val guiltyVotes = votes?.count { it.isGuilty } ?: 0
        val totalVotes = votes?.size ?: 0
        val isGuilty = guiltyVotes > (totalVotes / 2)

        val currentPlayers = players.firstOrNull()
        val gondiPlayers = currentPlayers?.filter { it.role == Role.GONDI }
        val accomplices = currentPlayers?.filter { it.role == Role.ACCOMPLICE }

        db.transaction {
            if (isGuilty) {
                game.accusedPlayer?.targetId?.let {
                    db.updateAliveStatus(false, it, round)
                }
            }

            if (round == 0L) {
                gondiPlayers?.forEach { player ->
                    db.updateKnownIdentities(
                        knownIdentities = gondiPlayers.map { it.toKnownIdentity(round) },
                        id = player.id,
                    )
                }
                accomplices?.forEach { accomplice ->
                    db.updateKnownIdentities(
                        knownIdentities = gondiPlayers?.map { it.toKnownIdentity(round) }
                            ?: error("Players cannot be null"),
                        id = accomplice.id,
                    )
                }
            }

            db.updateLastSaved(null, gameId = gameId)

            db.updatePendingKills(emptyList())
            db.clearVotes()
            db.clearAccused(gameId)
        }

        val updatedPlayers = players.firstOrNull()

        when (updatedPlayers?.checkWinner()) {
            Faction.GONDI -> {
                db.transaction {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.GONDI, gameId)
                }
            }
            Faction.VILLAGER -> {
                db.transaction {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.VILLAGER, gameId)
                }
            }
            else -> db.updatePhase(GamePhase.SLEEP, round + 1, gameId)
        }
    }

    private fun handleTownHallPhase(
        game: GameState,
        round: Long,
        gameId: String,
    ) {
        val lastSaved = game.lastSavedPlayerId
        val pendingKills = game.pendingKills

        db.transaction {
            val toEliminate = pendingKills.filterNot { it == lastSaved }
            if (toEliminate.isNotEmpty()) {
                db.updateAliveStatus(isAlive = false, ids = toEliminate, round)
            }
            db.updatePhase(GamePhase.TOWN_HALL, round, gameId)
        }
    }

    private suspend fun handleLobbyPhase(
        round: Long,
        gameId: String,
        players: Flow<List<Player>>,
    ){
        val gamePlayers = players.firstOrNull()

        val nonModerators = gamePlayers?.filter { player -> player.role != Role.MODERATOR }
        val playersWithRoles = nonModerators?.map { player -> player.copy(role = null) }
        db.transaction {
            playersWithRoles?.let { db.batchUpdatePlayerRole(it) }
            db.updatePhase(GamePhase.LOBBY, round, gameId)
        }

    }
}
