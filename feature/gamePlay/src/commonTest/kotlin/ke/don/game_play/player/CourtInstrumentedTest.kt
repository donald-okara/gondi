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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.state.GamePhase
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.screens.PlayerCourt
import ke.don.utils.Logger
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

/**
 * Instrumented test for the [PlayerCourt] composable, which handles the UI and logic
 * for the "Court" phase of the game from the player's perspective.
 *
 * This test class verifies the following scenarios:
 * - The voting modal appears correctly when the "Vote" button is clicked.
 * - The UI correctly prevents a player from voting twice in the same round.
 * - The UI correctly allows a player who hasn't voted yet to cast a vote.
 * - The UI displays a "dormant" state for players who are dead and cannot vote.
 *
 * It uses [TestGameRules] to set up and manage the game state for each test case.
 */
@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class CourtInstrumentedTest {
    val logger = Logger("CourtInstrumentedTest")

    @Test
    fun vote_showsVoteModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                ),
            ),
        )
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val votes = rules.votes
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.ShowVoteDialog -> {
                            clicked.value = true
                            rules.setUpPlayerState(
                                rules.playerState.value.copy(
                                    showVote = true,
                                ),
                            )
                        }
                        else -> {}
                    }
                }

                PlayerCourt(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                    votes = votes,
                )
            }
        }

        onNodeWithText("Vote").assertIsDisplayed()
        onNodeWithText("Vote").performClick()

        waitForIdle()

        assertTrue(clicked.value)
        onNodeWithText("You've already voted this round.").assertIsDisplayed()
    }

    @Test
    fun vote_showsVoteModalWhenPlayerHasntVoted() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                ),
            ),
        )
        val editedVotes = rules.votes.minus(
            rules.votes.find { vote -> vote.voterId == rules.currentPlayer.id }!!,
        )
        rules.setUpVotes(editedVotes)
        val clicked = mutableStateOf(false)
        val voted = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val votes = rules.votes
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.ShowVoteDialog -> {
                            clicked.value = true
                            rules.setUpPlayerState(
                                rules.playerState.value.copy(
                                    showVote = true,
                                ),
                            )
                        }
                        is PlayerHandler.Send -> {
                            voted.value = true
                        }
                        else -> {}
                    }
                }

                PlayerCourt(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                    votes = votes,
                )
            }
        }

        onNodeWithText("Vote").assertIsDisplayed()
        onNodeWithText("Vote").performClick()

        waitForIdle()

        assertTrue(clicked.value)
        onNodeWithText("Vote Guilty").assertIsDisplayed()
        onNodeWithText("Vote Guilty").performClick()

        waitForIdle()

        onNodeWithText("I am sure").assertIsDisplayed()
        onNodeWithText("I am sure").performClick()

        waitForIdle()

        assertTrue(voted.value)
    }

    @Test
    fun vote_showsDormantWhenPlayerIsDead() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                ),
            ),
        )
        val editedVotes = rules.votes.minus(
            rules.votes.find { vote -> vote.voterId == rules.currentPlayer.id }!!,
        )
        rules.setUpVotes(editedVotes)

        val editedPlayers = rules.players.map { player ->
            if (player.id == rules.currentPlayer.id) {
                player.copy(isAlive = false)
            } else {
                player
            }
        }
        rules.setUpPlayers(editedPlayers)
        logger.info(
            "Edited player: ${editedPlayers.find{it.id == rules.currentPlayer.id}}",
        )
        logger.info(
            "Current player: ${rules.currentPlayer}",
        )

        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val votes = rules.votes
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler) {
                    when (event) {
                        is PlayerHandler.ShowVoteDialog -> {
                            clicked.value = true
                            rules.setUpPlayerState(
                                rules.playerState.value.copy(
                                    showVote = true,
                                ),
                            )
                        }
                        else -> {}
                    }
                }

                PlayerCourt(
                    gameState = gameState,
                    myPlayer = rules.players.find { player -> player.id == currentPlayer.id }!!,
                    players = players,
                    playerState = playerState,
                    onEvent = ::onEvent,
                    votes = votes,
                )
            }
        }

        onNodeWithText("Vote").assertIsDisplayed()
        onNodeWithText("Vote").performClick()

        waitForIdle()

        assertTrue(clicked.value)

        onNodeWithText("Dead men tell no tales").assertIsDisplayed()
    }
}
