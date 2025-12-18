/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player

import WithTestLifecycle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.state.GamePhase
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.screens.MainPlayerContent
import ke.don.game_play.player.screens.PlayerSleep
import ke.don.resources.Resources
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import ke.don.utils.formatArgs
import org.jetbrains.compose.resources.stringResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class)
class SleepInstrumentedTest {
    val logger = Logger("SleepInstrumentedTest")

    @OptIn(ExperimentalTime::class)
    @Test
    fun selectPlayer_ShowsModalAndActionsWhenAlive() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
            ),
        )
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.SelectPlayer -> {
                            clicked.value = true
                            rules.setUpPlayerState(
                                rules.playerState.value.copy(
                                    selectedId = event.playerId,
                                ),
                            )
                        }
                        else -> {}
                    }
                }

                PlayerSleep(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                )
            }
        }

        val selectedPlayerName = rules.players.find { player -> player.id == "2" }?.name

        onNodeWithText(selectedPlayerName!!).assertIsDisplayed()
        onNodeWithText(selectedPlayerName).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)

        onNodeWithText("${ActionType.KILL.name.capitaliseFirst()} $selectedPlayerName").assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun selectPlayer_performsActionWhenClicked() = runComposeUiTest {
        val selectedId = "2"
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
            ),
        )
        rules.setUpPlayerState(
            rules.playerState.value.copy(
                selectedId = selectedId,
            ),
        )
        val clicked = mutableStateOf(false)
        val confirmationText = mutableStateOf("")
        val confirmText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer
                val selectedPlayerName = rules.players.find { player -> player.id == selectedId }?.name

                confirmationText.value = stringResource(Resources.Strings.GamePlay.CONFIRMATION_GONDI).formatArgs(selectedPlayerName)
                confirmText.value = stringResource(Resources.Strings.GamePlay.I_AM_SURE)

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.Send -> {
                            clicked.value = true
                        }
                        else -> {}
                    }
                }

                PlayerSleep(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                )
            }
        }

        val selectedPlayerName = rules.players.find { player -> player.id == selectedId }?.name

        onNodeWithText("${ActionType.KILL.name.capitaliseFirst()} $selectedPlayerName").assertIsDisplayed()
        onNodeWithText("${ActionType.KILL.name.capitaliseFirst()} $selectedPlayerName").performClick()

        waitForIdle()

        onNodeWithText(confirmationText.value).assertIsDisplayed()
        onNodeWithText(confirmText.value).performClick()

        assertEquals(true, clicked.value)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun selectPlayer_showsDormantText() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        val round = 2L
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
                round = round,
            ),
        )
        rules.setUpPlayerState(
            rules.playerState.value.copy(
                selectedId = selectedId,
            ),
        )
        val editedPlayer = rules.players.find { player -> player.id == rules.currentPlayer.id }?.copy(
            lastAction = PlayerAction(
                type = ActionType.KILL,
                round = round,
                playerId = rules.currentPlayer.id,
                targetId = selectedId,
            ),
        )
        rules.setUpPlayers(
            rules.players.map { player ->
                if (player.id == rules.currentPlayer.id) {
                    editedPlayer!!
                } else {
                    player
                }
            },
        )

        val dormantText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = players.find { it.id == rules.currentPlayer.id }!!

                dormantText.value = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_GONDI)

                PlayerSleep(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = {},
                )
            }
        }

        onNodeWithText(dormantText.value).assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun revealDeaths_showsDeadPlayers() = runComposeUiTest { // Reuse in moderator
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                ),
            ),
        )
        rules.setUpPlayerState(
            rules.playerState.value.copy(
                revealDeaths = true,
            ),
        )
        val editedPlayer = rules.players.find { player -> player.id == selectedId }?.copy(
            isAlive = false,
        )
        logger.info("Edited player =  $editedPlayer")
        rules.setUpPlayers(
            rules.players.map { player ->
                if (player.id == selectedId) {
                    editedPlayer!!
                } else {
                    player
                }
            },
        )

        logger.info(
            "New player =  ${rules.players.find { player -> player.id == selectedId }}",
        )

        val nightResultsText = mutableStateOf("")
        val killedPlayerText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                nightResultsText.value = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS)
                killedPlayerText.value = stringResource(Resources.Strings.GamePlay.KILLED_PLAYER)

                PlayerSleep(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = {},
                )
            }
        }

        onNodeWithText(nightResultsText.value).assertIsDisplayed()
        onNodeWithContentDescription(killedPlayerText.value).assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun onShowRules_showsRules() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)
        val showRules = mutableStateOf("")
        val gameObjectiveText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()

                showRules.value = stringResource(Resources.Strings.GamePlay.SHOW_RULES)
                gameObjectiveText.value = stringResource(Resources.Strings.GamePlay.GAME_OBJECTIVE)

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.ShowRulesModal -> {
                            rules.setUpPlayerState(rules.playerState.value.copy(showRulesModal = true))
                            clicked.value = true
                        }
                        else -> {}
                    }
                }

                MainPlayerContent(
                    gameState = gameState,
                    currentPlayer = rules.currentPlayer,
                    players = rules.players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                    onBack = {},
                    votes = emptyList(),
                )
            }
        }

        onNodeWithContentDescription(showRules.value).assertIsDisplayed()
        onNodeWithContentDescription(showRules.value).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)
        onNodeWithText(gameObjectiveText.value).assertIsDisplayed()
    }
}
