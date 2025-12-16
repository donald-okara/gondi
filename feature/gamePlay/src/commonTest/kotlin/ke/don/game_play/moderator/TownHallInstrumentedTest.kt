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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.state.GamePhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.game_play.moderator.screens.ModeratorTownHall
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.ExperimentalTime

/**
 * Instrumented tests for the Town Hall phase of the game from the moderator's perspective.
 * This class verifies the UI and state transitions for various scenarios within the `TOWN_HALL` game phase.
 * It uses `ComposeUiTest` to interact with and assert the state of the UI components.
 *
 * The tests cover:
 * - `revealDeaths_showsSavedPlayers`: Confirms that when night results are revealed, players who were saved are correctly indicated in the UI.
 * - `proceed_MovesToCourtWhenAccusationAndSecondArePresent`: Verifies that the moderator can advance the game to the `COURT` phase when a player has been accused and seconded.
 * - `proceed_MovesToSleepWhenAccusationAndSecondAreAbsent`: Checks that if no accusation is seconded, the moderator's "Proceed" action advances the game to the `SLEEP` phase.
 * - `exonerates_clearsAccused`: Ensures the moderator can clear an accusation against a player, resetting the accusation state.
 */
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
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                revealDeaths = true,
            ),
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                ModeratorTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = {},
                )
            }
        }

        onNodeWithText("Night Results").assertIsDisplayed()
        onNodeWithContentDescription("Saved player").assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun proceed_MovesToCourtWhenAccusationAndSecondArePresent() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()

        val selectedId = "2"
        val selectedPhase = mutableStateOf<GamePhase?>(null)

        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = "4",
                ),
                second = PlayerAction(
                    type = ActionType.SECOND,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = "5",
                ),
            ),
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AdvancePhase -> {
                                    selectedPhase.value = event.intent.phase
                                    logger.info("Passed phase: ${event.intent.phase}, set value: ${selectedPhase.value}")
                                    rules.setUpGameState(
                                        rules.gameState.value.copy(
                                            phase = event.intent.phase,
                                        ),
                                    )
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }
                }

                MainModeratorContent(
                    gameState = gameState,
                    hostPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = ::onEvent,
                    votes = emptyList(),
                    onBack = {},
                )
            }
        }

        val actionText = "Proceed"
        val expectedPhase = GamePhase.COURT

        val accuserId = rules.gameState.value.accusedPlayer?.playerId
        val accusedId = rules.gameState.value.accusedPlayer?.targetId
        val seconderId = rules.gameState.value.second?.playerId

        val accuser = rules.players.find { it.id == accuserId }
        val accused = rules.players.find { it.id == accusedId }
        val seconder = rules.players.find { it.id == seconderId }

        onNodeWithText(actionText).assertIsDisplayed()
        onNodeWithText("${accuser?.name} accuses ${accused?.name}").assertIsDisplayed()
        onNodeWithText("${seconder?.name} seconds the accusation").assertIsDisplayed()
        onNodeWithText(actionText).performClick()

        waitForIdle()

        assertEquals(expectedPhase, selectedPhase.value)
        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}").assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun proceed_MovesToSleepWhenAccusationAndSecondAreAbsent() = runComposeUiTest { // Reuse in moderator

        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
            ),
        )

        val selectedPhase = mutableStateOf<GamePhase?>(null)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AdvancePhase -> {
                                    selectedPhase.value = event.intent.phase
                                    logger.info("Passed phase: ${event.intent.phase}, set value: ${selectedPhase.value}")
                                    rules.setUpGameState(
                                        rules.gameState.value.copy(
                                            phase = event.intent.phase,
                                        ),
                                    )
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }
                }

                MainModeratorContent(
                    gameState = gameState,
                    hostPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = ::onEvent,
                    votes = emptyList(),
                    onBack = {},
                )
            }
        }

        val actionText = "Proceed"
        val expectedPhase = GamePhase.SLEEP

        onNodeWithText(actionText).assertIsDisplayed()
        onNodeWithText(actionText).performClick()

        waitForIdle()

        assertEquals(expectedPhase, selectedPhase.value)
        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}").assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun exonerates_clearsAccused() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()

        val selectedId = "2"

        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = "4",
                ),
            ),
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.ExoneratePlayer -> {
                                    rules.setUpGameState(
                                        rules.gameState.value.copy(
                                            accusedPlayer = null,
                                            second = null,
                                        ),
                                    )
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }
                }

                MainModeratorContent(
                    gameState = gameState,
                    hostPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = ::onEvent,
                    votes = emptyList(),
                    onBack = {},
                )
            }
        }

        val accusedPlayerId = rules.gameState.value.accusedPlayer?.targetId
        val accusedPlayer = rules.players.find { it.id == accusedPlayerId }

        val actionText = "Exonerate ${accusedPlayer?.name}"

        val accuserId = rules.gameState.value.accusedPlayer?.playerId
        val accusedId = rules.gameState.value.accusedPlayer?.targetId

        val accuser = rules.players.find { it.id == accuserId }
        val accused = rules.players.find { it.id == accusedId }

        onNodeWithText(actionText).assertIsDisplayed()

        onNodeWithText("${accuser?.name} accuses ${accused?.name}").assertIsDisplayed()
        onNodeWithText("Waiting for a second to proceed to court").assertIsDisplayed()
        onNodeWithText(actionText).performClick()

        waitForIdle()
        assertNull(rules.gameState.value.accusedPlayer)
    }
}
