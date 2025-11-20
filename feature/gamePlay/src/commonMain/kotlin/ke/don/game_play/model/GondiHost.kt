/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.local.datastore.ProfileStore
import ke.don.local.db.LocalDatabase
import ke.don.utils.result.LocalError
import ke.don.utils.result.Result
import ke.don.utils.result.ResultStatus
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GondiHost(
    private val server: LocalServer,
    private val database: LocalDatabase,
    private val profileStore: ProfileStore
) : ScreenModel {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    val hostPlayer = profileStore.profileFlow.map { it?.toPlayer() }

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes

    private val _moderatorState = MutableStateFlow(ModeratorState())
    val moderatorState: StateFlow<ModeratorState> = _moderatorState

    private var dbObserveJob: Job? = null

    private fun observeDatabase(gameId: String) {
        // Cancel any previous observation
        dbObserveJob?.cancel()

        dbObserveJob = screenModelScope.launch {
            combine(
                database.getGameState(gameId),
                database.getAllPlayers(),
                database.getAllVotes(),
            ) { state, players, votes ->
                Triple(state, players, votes)
            }.collect { (state, playerList, voteList) ->
                _gameState.value = state
                _players.value = playerList
                _votes.value = voteList
            }
        }
    }

    fun updateRoomName(
        name: String,
    ) {
        _moderatorState.update {
            it.copy(
                newGame = it.newGame.copy(id = name),
            )
        }
    }

    fun startServer() {
        screenModelScope.launch {
            handleModeratorCommand(ModeratorCommand.ResetGame(moderatorState.value.newGame.id))
            val identity = GameIdentity(
                id = moderatorState.value.newGame.id,
                gameName = moderatorState.value.newGame.name,
                moderatorName = hostPlayer.first()?.name ?: error("Player is not present"),
                moderatorAvatar = hostPlayer.first()?.avatar ?: error("Player is not present"),
                moderatorAvatarBackground = hostPlayer.first()?.background
                    ?: error("Player is not present"),
            )

            server.start(identity)
            hostPlayer.first()?.let {
                handleModeratorCommand(
                    ModeratorCommand.CreateGame(
                        moderatorState.value.newGame.id,
                        moderatorState.value.newGame,
                        it,
                    ),
                )
            }
                ?: error("Player is not present")

            observeDatabase(identity.id)
        }
    }

    fun updateAssignment(assignment: RoleAssignment) {
        _moderatorState.update { current ->
            current.copy(
                assignment = current.assignment
                    .filterNot { it.first == assignment.first } + assignment
            )
        }
    }

    fun assignRoles() {
        screenModelScope.launch {
            val moderatorId = hostPlayer.first()?.id ?: return@launch

            val nonModeratorPlayers = players.value.filterNot { it.id == moderatorId }

            assignRolesToPlayers(
                players = nonModeratorPlayers,
                assignments = moderatorState.value.assignment
            )
                .onSuccess { assigned ->
                    handleModeratorCommand(
                        ModeratorCommand.AssignRoleBatch(
                            moderatorState.value.newGame.id,
                            assigned
                        )
                    )

                    _moderatorState.update {
                        it.copy(assignmentsStatus = ResultStatus.Success(Unit))
                    }
                }
                .onFailure { error ->
                    _moderatorState.update {
                        it.copy(assignmentsStatus = ResultStatus.Error(error.message))
                    }
                }
        }
    }


    private fun assignRolesToPlayers(
        players: List<Player>,
        assignments: List<RoleAssignment>
    ): Result<List<Player>, LocalError> {

        val totalRequired = assignments.sumOf { it.second }

        if (players.size < totalRequired) {
            return Result.error(
                LocalError(
                    message = "Not enough players: need $totalRequired but have ${players.size}",
                    cause = "Not enough players",
                )
            )
        }

        // Build a mutable role bag
        val rolePool = buildList {
            assignments.forEach { (role, count) ->
                repeat(count) { add(role) }
            }
            // Surplus players → Villagers
            if (players.size > totalRequired) {
                repeat(players.size - totalRequired) { add(Role.VILLAGER) }
            }
        }.toMutableList()

        // Shuffle players
        val shuffledPlayers = players.shuffled()

        // Assign roles by randomly removing from the role pool
        val result = shuffledPlayers.map { player ->
            val role = rolePool.removeAt(rolePool.indices.random())
            player.copy(role = role)
        }

        return Result.success(result)
    }

    // Moderator actions
    fun onEvent(intent: ModeratorHandler){
        when(intent) {
            is ModeratorHandler.UpdateAssignments -> updateAssignment(intent.assignment)
            is ModeratorHandler.HandleModeratorCommand -> handleModeratorCommand(intent.intent)
            ModeratorHandler.StartServer -> startServer()
            is ModeratorHandler.UpdateRoomName -> updateRoomName(intent.name)
            ModeratorHandler.AssignRoles -> assignRoles()
        }
    }

    fun handleModeratorCommand(intent: ModeratorCommand) {
        screenModelScope.launch{
            val currentGameId = gameState.value?.id ?: moderatorState.value.newGame.id
            server.handleModeratorCommand(currentGameId, intent)
        }
    }

    override fun onDispose() {
        val targetGameId = gameState.value?.id ?: moderatorState.value.newGame.id
        dbObserveJob?.cancel()

        val cleanupScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        cleanupScope.launch {
            runCatching { database.clearPlayers() }.onFailure { it.printStackTrace() }
            runCatching { database.clearGameState() }.onFailure { it.printStackTrace() }
            runCatching { database.clearVotes() }.onFailure { it.printStackTrace() }
            runCatching {
                server.handleModeratorCommand(
                    targetGameId,
                    ModeratorCommand.ResetGame(targetGameId),
                )
            }.onFailure { it.printStackTrace() }
            runCatching { server.stop() }.onFailure { it.printStackTrace() }
        }

        super.onDispose()
    }
}
