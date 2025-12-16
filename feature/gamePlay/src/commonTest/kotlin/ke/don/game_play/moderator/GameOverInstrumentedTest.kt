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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.Faction
import ke.don.domain.state.GamePhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

/**
 * Test cases for *Game Over*
 * * "Play again" is *displayed* for moderator
 * * Winning faction name is displayed
 * * "Play again" moves to sleep
 */
@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class GameOverInstrumentedTest {
    private val logger = Logger("GameOverInstrumentedTest")

    @Test
    fun winningFactionAndPlayAgainAreDisplayed() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val winningFaction = Faction.GONDI
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.GAME_OVER,
                winners = winningFaction,
            ),
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                MainModeratorContent(
                    gameState = gameState,
                    hostPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = {},
                    votes = emptyList(),
                    onBack = {},
                )
            }
        }

        onNodeWithText("Play again").assertIsDisplayed()
        onNodeWithText("${winningFaction.name.capitaliseFirst()}s Win!").assertIsDisplayed()
    }

    @Test
    fun playAgain_movesToSleep() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val winningFaction = Faction.GONDI
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.GAME_OVER,
                winners = winningFaction,
            ),
        )
        val selectedPhase = mutableStateOf(GamePhase.GAME_OVER)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            selectedPhase.value = GamePhase.SLEEP
                            rules.setUpGameState(
                                gameState.copy(
                                    phase = selectedPhase.value,
                                ),
                            )
                        }
                        else -> {
                            logger.info("Event $event is not handled")
                        }
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

        onNodeWithText("Play again").assertIsDisplayed()
        onNodeWithText("Play again").performClick()

        waitForIdle()
        assertEquals(GamePhase.SLEEP, selectedPhase.value)
    }
}
