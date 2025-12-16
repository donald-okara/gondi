/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ServerId
import ke.don.domain.state.GamePhase
import ke.don.game_play.player.di.GAME_PLAYER_SCOPE
import ke.don.game_play.player.useCases.GameClientManager
import ke.don.game_play.player.useCases.GameClientState
import ke.don.game_play.player.useCases.GamePlayerController
import ke.don.utils.Logger
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.onFailure
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class GondiClient(
    private val koin: Koin,
) : ScreenModel, KoinScopeComponent {

    override val scope: Scope by lazy {
        koin.createScope(
            Uuid.random().toString(),
            named(GAME_PLAYER_SCOPE),
        )
    }

    private val clientState by lazy { scope.get<GameClientState>() }
    private val clientManager by lazy { scope.get<GameClientManager>() }
    private val controller by lazy { scope.get<GamePlayerController>() }

    val logger = Logger("GondiClient")

    val currentPlayer = clientState.currentPlayer
    val gameState = clientState.gameState
    val players = clientState.players
    val votes = clientState.votes
    val playerState = clientState.playerState

    init {
        screenModelScope.launch {
            gameState
                .map { it?.phase } // Only care about the phase
                .distinctUntilChanged() // Only react when phase *actually* changes
                .collect { newPhase ->
                    // Reset selectedId whenever phase changes
                    clientState.updatePlayerState { it.copy(selectedId = null) }

                    // Always reveal deaths on town hall and sleep except on round 0L for the latter
                    if (newPhase == GamePhase.TOWN_HALL || (newPhase == GamePhase.SLEEP && gameState.value?.round != 0L)) {
                        clientState.updatePlayerState { it.copy(revealDeaths = true) }
                    }
                }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onEvent(intent: PlayerHandler) {
        when (intent) {
            is PlayerHandler.Connect -> connect(intent.serverId)
            PlayerHandler.RevealDeaths -> clientState.updatePlayerState { it.copy(revealDeaths = !it.revealDeaths) }
            is PlayerHandler.Send -> sendIntent(intent.message)
            PlayerHandler.ShowLeaveDialog -> clientState.updatePlayerState { it.copy(showLeaveGame = !it.showLeaveGame) }
            PlayerHandler.ShowVoteDialog -> clientState.updatePlayerState { it.copy(showVote = !it.showVote) }
            PlayerHandler.ShowRulesModal -> screenModelScope.launch{
                currentPlayer.firstOrNull()?.captureEvent(
                    if (playerState.value.showRulesModal) "Closed rules modal" else "Opened rules modal"
                )
                clientState.updatePlayerState { it.copy(showRulesModal = !it.showRulesModal) }
            }
            is PlayerHandler.SelectPlayer -> clientState.updatePlayerState { it.copy(selectedId = intent.playerId) }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun connect(serverId: ServerId) {
        screenModelScope.launch {
            clientState.updatePlayerState {
                it.copy(
                    connectionStatus = ReadStatus.Loading,
                )
            }

            clientManager.connect(serverId).onFailure { error ->
                Matcha.showErrorToast(
                    title = "Connection failed",
                    message = error.message,
                    retryAction = { connect(serverId) },
                )
            }
        }
    }

    fun sendIntent(intent: PlayerIntent) {
        screenModelScope.launch {
            controller.sendIntent(intent)
        }
    }

    override fun onDispose() {
        runBlocking { clientManager.dispose() }
        clientState.clearState()
        scope.close()
        super.onDispose()
    }
}
