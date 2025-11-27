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
import ke.don.game_play.player.di.GAME_PLAYER_SCOPE
import ke.don.utils.Logger
import ke.don.utils.result.ResultStatus
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.component.KoinScopeComponent
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class GondiHost(
    private val koin: Koin
) : ScreenModel, KoinScopeComponent {
    override val scope: Scope by lazy {
        koin.createScope(
            Uuid.random().toString(),
            named(GAME_PLAYER_SCOPE)
        )
    }

    private val session by lazy { scope.get<GameSessionState>() }
    private val moderator by lazy { scope.get<GameModeratorController>() }
    private val serverManager by lazy { scope.get<GameServerManager>() }

    private val logger = Logger("GondiHost")
    val gameState = session.gameState.asStateFlow()

    val players = session.players.asStateFlow()

    val hostPlayer = session.hostPlayer

    val votes = session.votes.asStateFlow()

    val moderatorState = session.moderatorState.asStateFlow()

    init {
        serverManager.observeLocalAnnouncements {
            Matcha.info(
                description = it,
                title = "Announcement",
            )
            session.updateModeratorState { state ->
                state.copy(announcements = state.announcements + (it to Clock.System.now()))
            }
        }
    }

    fun startServer() {
        session.updateModeratorState {
            it.copy(createStatus = ResultStatus.Loading)
        }
        moderator.validateAssignments().onSuccess {
            screenModelScope.launch {
                val host = hostPlayer.first() ?: error("Player is not present")

                val identity = GameIdentity(
                    id = moderatorState.value.newGame.id,
                    gameName = moderatorState.value.newGame.name,
                    moderatorName = host.name,
                    moderatorAvatar = host.avatar ?: error("Player avatar is not present"),
                    moderatorAvatarBackground = host.background,
                )
                serverManager.startServer(
                    identity,
                    moderatorState.value.newGame.copy(availableSlots = moderatorState.value.assignment.sumOf { it.second }.toLong()),
                    host,
                ).onFailure { error ->
                    Matcha.showErrorToast(
                        message = error.message,
                        title = "Error",
                        retryAction = { startServer() },
                    )
                    session.updateModeratorState { state ->
                        state.copy(createStatus = ResultStatus.Error(error.message))
                    }
                }.onSuccess {
                    session.updateModeratorState {
                        it.copy(createStatus = ResultStatus.Success(Unit))
                    }
                    session.observe(identity.id, screenModelScope)
                }
            }
        }.onFailure { error ->
            session.updateModeratorState {
                it.copy(createStatus = ResultStatus.Error(error.message))
            }
            Matcha.showErrorToast(
                message = error.message,
                title = "Error",
                retryAction = { startServer() },
            )
        }
    }

    fun onEvent(intent: ModeratorHandler) {
        when (intent) {
            is ModeratorHandler.UpdateAssignments -> moderator.updateAssignment(intent.assignment)
            is ModeratorHandler.HandleModeratorCommand -> moderator.handleCommand(intent.intent, screenModelScope)
            ModeratorHandler.StartServer -> startServer()
            is ModeratorHandler.UpdateRoomName -> moderator.updateRoomName(intent.name)
            ModeratorHandler.ShowLeaveDialog -> session.updateModeratorState {
                it.copy(showLeaveGame = !it.showLeaveGame)
            }
            is ModeratorHandler.SelectPlayer -> moderator.selectPlayer(intent.id)
            ModeratorHandler.StartGame -> {
                startGame()
            }
            ModeratorHandler.ShowRulesModal -> {
                session.updateModeratorState {
                    it.copy(showRulesModal = !it.showRulesModal)
                }
            }
        }
    }

    fun startGame() {
        screenModelScope.launch {
            // Wait for role assignment to complete
            moderator.assignRoles().onSuccess {
                // Wait for the players flow to reflect the changes from assignRoles
                val updatedPlayers = players.first { list -> list.all { it.role != null || !it.isAlive } }
                if (updatedPlayers.isNotEmpty()) {
                    gameState.firstOrNull()?.let {
                        moderator.handleCommand(
                            ModeratorCommand.StartGame(it.id),
                            screenModelScope,
                        )
                    }
                }
            }.onFailure {
                logger.error(it.message)
                Matcha.showErrorToast(
                    message = it.message,
                    title = "Error",
                    retryAction = { startGame() },
                )
            }
        }
    }

    override fun onDispose() {
        screenModelScope.launch{
            session.stopObserving()
            serverManager.stopServer()
        }
        super.onDispose()
    }
}
