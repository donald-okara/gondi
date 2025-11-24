/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.game_play.moderator.useCases.GameModeratorController
import ke.don.game_play.moderator.useCases.GameServerManager
import ke.don.game_play.moderator.useCases.GameSessionState
import ke.don.utils.Logger
import ke.don.utils.result.ResultStatus
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GondiHost(
    private val session: GameSessionState,
    private val moderator: GameModeratorController,
    private val serverManager: GameServerManager
) : ScreenModel {
    private val logger = Logger("GondiHost")
    val gameState = session.gameState.asStateFlow()

    val players = session.players.asStateFlow()

    val hostPlayer = session.hostPlayer

    val votes = session.votes.asStateFlow()

    val moderatorState = session.moderatorState.asStateFlow()

    fun startServer() {
        session.updateModeratorState {
            it.copy(createStatus = ResultStatus.Loading)
        }
        moderator.validateAssignments().onSuccess {
            screenModelScope.launch {
                val identity = GameIdentity(
                    id = moderatorState.value.newGame.id,
                    gameName = moderatorState.value.newGame.name,
                    moderatorName = hostPlayer.first()?.name ?: error("Player is not present"),
                    moderatorAvatar = hostPlayer.first()?.avatar ?: error("Player is not present"),
                    moderatorAvatarBackground = hostPlayer.first()?.background
                        ?: error("Player is not present"),
                )
                runCatching{
                    serverManager.startServer(
                        identity,
                        moderatorState.value.newGame,
                        screenModelScope,
                        hostPlayer.first()!!
                    )
                    session.observe(identity.id, screenModelScope)
                    session.updateModeratorState {
                        it.copy(createStatus = ResultStatus.Success(Unit))
                    }
                }.onFailure { error ->
                    session.updateModeratorState {
                        it.copy(createStatus = ResultStatus.Error(error.message.toString()))
                    }
                    Matcha.showErrorToast(
                        message = error.message.toString(),
                        title = "Error",
                        retryAction = { startServer() }
                    )
                }
            }
        }.onFailure { error ->
            session.updateModeratorState {
                it.copy(createStatus = ResultStatus.Error(error.message))
            }
            Matcha.showErrorToast(
                message = error.message,
                title = "Error",
                retryAction = { startServer() }
            )
        }
    }

    fun onEvent(intent: ModeratorHandler){
        when(intent) {
            is ModeratorHandler.UpdateAssignments -> moderator.updateAssignment(intent.assignment)
            is ModeratorHandler.HandleModeratorCommand -> moderator.handleCommand(intent.intent, screenModelScope)
            ModeratorHandler.StartServer -> startServer()
            is ModeratorHandler.UpdateRoomName -> moderator.updateRoomName(intent.name)
            ModeratorHandler.ShowLeaveDialog -> session.updateModeratorState {
                it.copy(showLeaveGame = !it.showLeaveGame)
            }
            ModeratorHandler.StartGame -> {
                startGame()
            }
        }
    }

    fun startGame(){
        screenModelScope.launch {
            // Wait for role assignment to complete
            moderator.assignRoles().onSuccess {
                // Wait for the players flow to reflect the changes from assignRoles
                val updatedPlayers = players.first { list -> list.all { it.role != null || !it.isAlive } }
                if (updatedPlayers.isNotEmpty()) {
                    gameState.firstOrNull()?.let {
                        moderator.handleCommand(
                            ModeratorCommand.StartGame(it.id),
                            screenModelScope
                        )
                    }
                }
            }.onFailure {
                logger.error(it.message)
                Matcha.showErrorToast(
                    message = it.message,
                    title = "Error",
                    retryAction = {startGame()}
                )
            }

        }
    }

    override fun onDispose() {
        session.stopObserving()
        serverManager.stopServer(screenModelScope)

        super.onDispose()
    }
}
