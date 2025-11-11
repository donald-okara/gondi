/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.usecase

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import ke.don.components.helpers.Matcha
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ClientUpdate
import ke.don.domain.gameplay.server.ServerUpdate
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.remote.server.ClientObject.client
import ke.don.utils.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GondiClient() : ScreenModel {

    val logger = Logger("GondiClient")

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes.asStateFlow()

    @OptIn(ExperimentalTime::class)
    private val _lastPing = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val lastPing: StateFlow<Long> = _lastPing.asStateFlow()

    private var session: DefaultClientWebSocketSession? = null

    suspend fun connect(host: String, port: Int) {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Reconnecting"))
        screenModelScope.connectWithRetry(host, port)
    }

    fun sendIntent(intent: PlayerIntent) {
        screenModelScope.launch {
            val message = ClientUpdate.PlayerIntentMsg(intent)
            session?.send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), message)))
                ?: logger.error("⚠️ Not connected to server")
        }
    }

    fun disconnect() {
        screenModelScope.launch {
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client exit"))
            session = null
        }
    }

    fun startPing() = screenModelScope.launch {
        while (isActive && session != null) {
            delay(10_000)
            session?.send(Frame.Text(Json.encodeToString(ClientUpdate.serializer(), ClientUpdate.Ping)))
        }
    }

    override fun onDispose() {
        super.onDispose()

        // Gracefully close any active session
        screenModelScope.launch {
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "Disposed"))
        }

        session = null

        // Cancel all running coroutines tied to this model
        screenModelScope.cancel()

        // Reset state
        _gameState.value = null
        _players.value = emptyList()
        _votes.value = emptyList()
    }

    private fun CoroutineScope.connectWithRetry(host: String, port: Int, maxRetries: Int = 5) = launch {
        var attempt = 0
        while (isActive && attempt < maxRetries) {
            try {
                connectOnce(host, port)
                return@launch
            } catch (e: Exception) {
                attempt++
                val delayTime = (attempt * 2_000L).coerceAtMost(10_000L)
                delay(delayTime)
            }
        }
    }

    private suspend fun connectOnce(host: String, port: Int) {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Reconnecting"))
        try {
            client.webSocket("ws://$host:$port/game") {
                session = this
                logger.info("Connected ✅")

                for (frame in incoming) {
                    if (frame is Frame.Text) handleServerUpdate(frame.readText())
                }
            }
        } catch (e: Exception) {
            logger.error("Connection error: ${e.message}")
            throw e // bubble up to retry
        } finally {
            logger.info("Disconnected")
            Matcha.error(title = "Disconnected", description = "Disconnected from server")
            session = null
        }
    }

    private fun handleServerUpdate(json: String) {
        val update = Json.decodeFromString<ServerUpdate>(json)
        when (update) {
            is ServerUpdate.GameStateSnapshot -> _gameState.value = update.state
            is ServerUpdate.PlayersSnapshot -> _players.value = update.players
            is ServerUpdate.VotesSnapshot -> _votes.value = update.votes
            is ServerUpdate.Error -> {
                logger.error("❌ ${update.message}")
                Matcha.error(title = "Error", description = update.message)
            }
            is ServerUpdate.Forbidden -> {
                logger.warn("❌ ${update.message}")
                Matcha.warning(title = "Forbidden", description = update.message)
            }
            is ServerUpdate.Announcement -> {
                logger.info("📣 ${update.message}")
                Matcha.info(title = "Announcement", description = update.message)
            }
            is ServerUpdate.LastPing -> _lastPing.update { update.long }
        }
    }
}
