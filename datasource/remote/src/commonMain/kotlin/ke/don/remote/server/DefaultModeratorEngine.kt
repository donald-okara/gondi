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

        when (command) {
            is ModeratorCommand.CreateGame -> {
                db.transaction {
                    db.insertOrReplaceGameState(command.game)
                    db.insertOrReplacePlayer(command.player)
                }
            }

            is ModeratorCommand.AdvancePhase -> game?.let { currentGame ->
                handlePhaseAdvance(command, currentGame, players, currentRound)
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
            GamePhase.TOWN_HALL -> handleTownHallPhase(game, command.phase, round, gameId)
            GamePhase.SLEEP -> handleSleepPhase(game, command.phase, round, players, gameId)
            else -> db.updatePhase(command.phase, round, gameId)
        }
    }

    private fun handleTownHallPhase(
        game: GameState,
        phase: GamePhase,
        round: Long,
        gameId: String,
    ) {
        val lastSaved = game.lastSavedPlayerId
        val pendingKills = game.pendingKills

        db.transaction {
            val toEliminate = pendingKills.filterNot { it == lastSaved }
            if (toEliminate.isNotEmpty()) {
                db.updateAliveStatus(isAlive = false, ids = toEliminate)
            }
            db.updateLastSaved(null)
            db.updatePhase(phase, round, gameId)
        }
    }

    private suspend fun handleSleepPhase(
        game: GameState,
        phase: GamePhase,
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
                    db.updateAliveStatus(false, it)
                }
            }

            if (round == 0L) {
                gondiPlayers?.forEach { player ->
                    db.updateKnownIdentities(
                        knownIdentities = gondiPlayers.map { it.toKnownIdentity() },
                        id = player.id
                    )
                }
                accomplices?.forEach { accomplice ->
                    db.updateKnownIdentities(
                        knownIdentities = gondiPlayers?.map { it.toKnownIdentity() }
                            ?: error("Players cannot be null"),
                        id = accomplice.id
                    )
                }
            }

            db.updatePendingKills(emptyList())
        }

        val updatedPlayers = players.firstOrNull()

        when (updatedPlayers?.checkWinner()) {
            Faction.GONDI -> {
                db.transaction {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.GONDI)
                }
            }
            Faction.VILLAGER -> {
                db.transaction {
                    db.updatePhase(GamePhase.GAME_OVER, 0L, gameId)
                    db.updateWinners(Faction.VILLAGER)
                }
            }
            else -> db.updatePhase(phase, round + 1, gameId)
        }
    }

}
