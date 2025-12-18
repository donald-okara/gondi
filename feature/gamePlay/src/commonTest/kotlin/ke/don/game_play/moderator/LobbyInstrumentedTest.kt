/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator

import WithTestLifecycle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.game_play.moderator.useCases.PLAYER_LOWER_LIMIT
import ke.don.resources.Resources
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import org.jetbrains.compose.resources.stringResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.ExperimentalTime

/**
 * Instrumented tests for the game Lobby screen as seen by the moderator.
 * This class tests the UI and interactions within the [MainModeratorContent] composable
 * when the game phase is [GamePhase.LOBBY].
 *
 * It covers scenarios such as:
 * - Starting the game with sufficient, insufficient, and too few players.
 * - Selecting a player to bring up moderator actions (assign role, remove).
 * - Performing moderator actions and verifying the UI state changes.
 * - Displaying the game rules modal.
 *
 * These tests use [TestGameRules] to set up and manage the test environment, including
 * game state, moderator state, and player data.
 */
@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class LobbyInstrumentedTest {
    val logger = Logger("LobbyInstrumentedTest")

    @Test
    fun advancePhaseMovesToSleep_SuccessWhenPlayersAreEnough() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)
        val expectedButtonText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                expectedButtonText.value = stringResource(Resources.Strings.GamePlay.START_GAME)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        ModeratorHandler.StartGame -> {
                            clicked.value = true
                            rules.setUpGameState(rules.gameState.value.copy(phase = GamePhase.SLEEP))

                            logger.info(
                                "State Phase: ${gameState.phase.name.capitaliseFirst()} Live Phase: ${rules.gameState.value.phase.name.capitaliseFirst()}",
                            )
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
                )
            }
        }

        val expectedPhase = GamePhase.SLEEP

        onNodeWithText(expectedButtonText.value).assertIsDisplayed()
        onNodeWithText(expectedButtonText.value).assertIsEnabled()
        onNodeWithText(expectedButtonText.value).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)
        assertEquals(expectedPhase, rules.gameState.value.phase)
        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}").assertIsDisplayed()
    }

    @Test
    fun advancePhaseMovesToSleep_SuccessWhenPlayersAreInadequate() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpPlayers(
            rules.players.take(rules.gameState.value.availableSlots.toInt() - 1),
        )
        val clicked = mutableStateOf(false)
        val expectedButtonText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                expectedButtonText.value =
                    Resources.Strings.GamePlay.startWithPlayers(rules.players.size)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        ModeratorHandler.StartGame -> {
                            clicked.value = true
                            rules.setUpGameState(rules.gameState.value.copy(phase = GamePhase.SLEEP))

                            logger.info(
                                "State Phase: ${gameState.phase.name.capitaliseFirst()} Live Phase: ${rules.gameState.value.phase.name.capitaliseFirst()}",
                            )
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
                )
            }
        }

        val expectedPhase = GamePhase.SLEEP

        onNodeWithText(expectedButtonText.value).assertIsDisplayed()
        onNodeWithText(expectedButtonText.value).assertIsEnabled()
        onNodeWithText(expectedButtonText.value).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)
        assertEquals(expectedPhase, rules.gameState.value.phase)
        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}").assertIsDisplayed()
    }

    @Test
    fun advancePhaseMovesToSleep_buttonDisabled() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpPlayers(
            rules.players.take(PLAYER_LOWER_LIMIT - 1),
        )
        val expectedButtonText = mutableStateOf("")

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                expectedButtonText.value =
                    Resources.Strings.GamePlay.startWithPlayers(rules.players.size)

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = {},
                    onBack = {},
                )
            }
        }

        onNodeWithText(expectedButtonText.value).assertIsDisplayed()
        onNodeWithText(expectedButtonText.value).assertIsNotEnabled()
    }

    @Test
    fun onSelectPlayer_displaysModal() = runComposeUiTest {
        val assignText = mutableStateOf("")
        val removeText = mutableStateOf("")

        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                assignText.value = stringResource(Resources.Strings.GamePlay.ASSIGN_ROLE)
                removeText.value = stringResource(Resources.Strings.GamePlay.REMOVE)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.SelectPlayer -> {
                            clicked.value = true
                            rules.setUpModeratorState(
                                rules.moderatorState.value.copy(
                                    selectedPlayerId = event.id,
                                ),
                            )

                            logger.info(
                                "State Selected Id: ${moderatorState.selectedPlayerId} Live Selected Id: ${rules.moderatorState.value.selectedPlayerId}",
                            )
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
                )
            }
        }

        val playerButtonText = "Stefon Zelesky"

        onNodeWithText(playerButtonText).assertIsDisplayed()
        onNodeWithText(playerButtonText).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)
        onNodeWithText(assignText.value).assertIsDisplayed()
        onNodeWithText(removeText.value).assertIsDisplayed()
    }

    @Test
    fun assign_assignDismissesModal() = runComposeUiTest {
        val assignText = mutableStateOf("")
        val removeText = mutableStateOf("")

        val selectedId = "1"
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                selectedPlayerId = selectedId,
            ),
        )
        val roleAssigned = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                assignText.value = stringResource(Resources.Strings.GamePlay.ASSIGN_ROLE)
                removeText.value = stringResource(Resources.Strings.GamePlay.REMOVE)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AssignRole -> {
                                    roleAssigned.value = true
                                }

                                else -> {}
                            }
                        }

                        is ModeratorHandler.SelectPlayer -> {
                            rules.setUpModeratorState(
                                moderatorState.copy(selectedPlayerId = event.id),
                            )
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
                )
            }
        }

        val roleText = Role.GONDI.name.capitaliseFirst()

        onNodeWithText(assignText.value).assertIsDisplayed()
        onNodeWithText(removeText.value).assertIsDisplayed()

        onNodeWithText(assignText.value).performClick()
        onNodeWithText(roleText).assertIsDisplayed()
        onNodeWithText(roleText).performClick()

        waitForIdle()

        assertNull(rules.moderatorState.value.selectedPlayerId)
        assertEquals(true, roleAssigned.value)
    }

    @Test
    fun assign_removeDismissesModal() = runComposeUiTest {
        val assignText = mutableStateOf("")
        val removeText = mutableStateOf("")
        val confirmationText = mutableStateOf("")

        val selectedId = "1"
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                selectedPlayerId = selectedId,
            ),
        )
        val removed = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                assignText.value = stringResource(Resources.Strings.GamePlay.ASSIGN_ROLE)
                removeText.value = stringResource(Resources.Strings.GamePlay.REMOVE)
                confirmationText.value = stringResource(Resources.Strings.GamePlay.I_AM_SURE)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.RemovePlayer -> {
                                    removed.value = true
                                }

                                else -> {}
                            }
                        }

                        is ModeratorHandler.SelectPlayer -> {
                            rules.setUpModeratorState(
                                moderatorState.copy(selectedPlayerId = event.id),
                            )
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
                )
            }
        }

        onNodeWithText(assignText.value).assertIsDisplayed()
        onNodeWithText(removeText.value).assertIsDisplayed()

        onNodeWithText(removeText.value).performClick()
        onNodeWithText(confirmationText.value).assertIsDisplayed()
        onNodeWithText(confirmationText.value).performClick()

        waitForIdle()

        assertNull(rules.moderatorState.value.selectedPlayerId)
        assertEquals(true, removed.value)
    }

    @Test
    fun onShowRules_displaysModal() = runComposeUiTest {
        val showRules = mutableStateOf("")
        val gameObjectiveText = mutableStateOf("")

        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                showRules.value = stringResource(Resources.Strings.GamePlay.SHOW_RULES)
                gameObjectiveText.value = stringResource(Resources.Strings.GamePlay.GAME_OBJECTIVE)

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.ShowRulesModal -> {
                            clicked.value = true
                            rules.setUpModeratorState(rules.moderatorState.value.copy(showRulesModal = rules.moderatorState.value.showRulesModal.not()))
                        }

                        else -> {}
                    }
                }

                MainModeratorContent(
                    moderatorState = moderatorState,
                    gameState = gameState,
                    hostPlayer = rules.currentPlayer,
                    players = rules.players,
                    votes = rules.votes,
                    onEvent = ::onEvent,
                    onBack = {},
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
