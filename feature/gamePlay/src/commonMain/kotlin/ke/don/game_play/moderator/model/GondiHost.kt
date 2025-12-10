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
import ke.don.game_play.BuildConfig.VERSION_NAME
import ke.don.game_play.moderator.di.GAME_MODERATOR_SCOPE
import ke.don.game_play.moderator.useCases.GameModeratorController
import ke.don.game_play.moderator.useCases.GameServerManager
import ke.don.game_play.moderator.useCases.GameSessionState
import ke.don.utils.Logger
import ke.don.utils.result.ResultStatus
import ke.don.utils.result.onFailure
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private val koin: Koin,
) : ScreenModel, KoinScopeComponent {
    override val scope: Scope by lazy {
        koin.createScope(
            Uuid.random().toString(),
            named(GAME_MODERATOR_SCOPE),
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
        screenModelScope.launch {
            serverManager.announcements.collect { event ->
                Matcha.info(event.message)
                session.updateModeratorState {
                    val updated = it.announcements + (event.message to Clock.System.now())
                    it.copy(announcements = updated.takeLast(MAX_ANNOUNCEMENTS))
                }
            }
        }
    }

    fun startServer() {
        session.updateModeratorState {
            it.copy(createStatus = ResultStatus.Loading)
        }
        moderator.validateAssignments().onSuccess {
            screenModelScope.launch {
                val host = session.profileSnapshot.first()?.toPlayer() ?: error("Player is not present")

                val identity = GameIdentity(
                    id = moderatorState.value.newGame.id,
                    gameName = moderatorState.value.newGame.name,
                    moderatorName = host.name,
                    version = VERSION_NAME,
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
            is ModeratorHandler.HandleModeratorCommand -> screenModelScope.launch {
                serverManager.handleCommand(intent.intent)
            }
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
            moderator.assignRoles().onSuccess { players ->
                gameState.value?.let {
                    try {
                        serverManager.handleCommand(
                            ModeratorCommand.AssignRoleBatch(
                                it.id,
                                players,
                            ),
                        )
                        serverManager.handleCommand(ModeratorCommand.StartGame(it.id))
                    } catch (e: Exception) {
                        logger.error("Failed to start game: ${e.message}")
                        Matcha.showErrorToast(
                            message = e.message ?: "Failed to start game",
                            title = "Error",
                            retryAction = { startGame() },
                        )
                    }
                }
            }.onFailure { error ->
                logger.error(error.message)
                session.updateModeratorState {
                    it.copy(
                        assignmentsStatus = ResultStatus.Error(error.message),
                    )
                }
                Matcha.showErrorToast(
                    message = error.message,
                    title = "Error",
                    retryAction = { startGame() },
                )
            }
        }
    }

    override fun onDispose() {
        session.stopObserving()
        runBlocking { serverManager.stopServer() }
        scope.close()
        super.onDispose()
    }
}

const val MAX_ANNOUNCEMENTS = 10
