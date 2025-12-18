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
import ke.don.game_play.player.screens.PlayerTownHall
import ke.don.resources.Resources
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import org.jetbrains.compose.resources.stringResource
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class)
class TownHallInstrumentedTest {
    val logger = Logger("TownHallInstrumentedTest")

    @OptIn(ExperimentalTime::class)
    @Test
    fun revealDeaths_showsSavedPlayers() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                pendingKills = listOf(selectedId),
                lastSavedPlayerId = selectedId,
            ),
        )
        rules.setUpPlayerState(
            rules.playerState.value.copy(
                revealDeaths = true,
            ),
        )

        val nightResultsText = mutableStateOf("")
        val savedPlayerText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                nightResultsText.value = stringResource(Resources.Strings.GamePlay.NIGHT_RESULTS)
                savedPlayerText.value = stringResource(Resources.Strings.GamePlay.SAVED_PLAYER)

                PlayerTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = {},
                )
            }
        }

        onNodeWithText(nightResultsText.value).assertIsDisplayed()
        onNodeWithContentDescription(savedPlayerText.value).assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun selectPlayer_showsModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
            ),
        )

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
                        }
                        else -> {}
                    }
                }

                PlayerTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                )
            }
        }

        val selectedPlayer = rules.players.find { it.id == selectedId }

        onNodeWithText(selectedPlayer?.name!!).assertIsDisplayed()
        onNodeWithText(selectedPlayer.name).performClick()

        waitForIdle()

        assertTrue(clicked.value)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun accusePlayer_setsAccusation() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
            ),
        )

        rules.setUpPlayerState(
            rules.playerState.value.copy(
                selectedId = selectedId,
            ),
        )

        val confirmationText = mutableStateOf("")
        val confirmText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer
                val selectedPlayer = rules.players.find { it.id == selectedId }

                confirmationText.value =
                    Resources.Strings.GamePlay.accusePlayerConfirmation(selectedPlayer?.name!!)
                confirmText.value = stringResource(Resources.Strings.GamePlay.I_AM_SURE)

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.Send -> {
                            clicked.value = true
                        }
                        else -> {}
                    }
                }

                PlayerTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                )
            }
        }

        val selectedPlayer = rules.players.find { it.id == selectedId }

        onNodeWithText("${ActionType.ACCUSE.name.capitaliseFirst()} ${selectedPlayer?.name}").assertIsDisplayed()
        onNodeWithText("Accuse ${selectedPlayer?.name}").performClick()

        waitForIdle()

        onNodeWithText(confirmationText.value).assertIsDisplayed()
        onNodeWithText(confirmText.value).performClick()

        waitForIdle()

        assertTrue(clicked.value)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun secondPlayer_setsSecond() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)

        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = "5",
                ),
            ),
        )

        val waitingText = mutableStateOf("")
        val secondText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                waitingText.value = stringResource(Resources.Strings.GamePlay.WAITING_FOR_SECOND)
                secondText.value = stringResource(Resources.Strings.GamePlay.SECOND_THE_ACCUSATION)

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.Send -> {
                            clicked.value = true
                        }
                        else -> {}
                    }
                }

                PlayerTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                )
            }
        }

        onNodeWithText(waitingText.value).assertIsDisplayed()
        onNodeWithText(secondText.value).assertIsDisplayed()
        onNodeWithText(secondText.value).performClick()

        waitForIdle()

        assertTrue(clicked.value)
    }
}
