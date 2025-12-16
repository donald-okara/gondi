package ke.don.game_play.moderator

import WithTestLifecycle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.actionType
import ke.don.domain.state.GamePhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.game_play.moderator.screens.ModeratorSleep
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.time.ExperimentalTime

/**
 * Instrumented tests for the moderator's view during the [GamePhase.SLEEP] phase.
 * This class verifies the UI and state transitions related to the sleep phase, such as
 * revealing deaths and proceeding to the next game phase.
 *
 * It uses [TestGameRules] to set up and manage the game state for each test scenario.
 */
@OptIn(ExperimentalTestApi::class)
class SleepInstrumentedTest {
    val logger = Logger("SleepInstrumentedTest")

    @OptIn(ExperimentalTime::class)
    @Test
    fun revealDeaths_showsDeadPlayers() = runComposeUiTest {// Reuse in moderator
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId
                )
            )
        )
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                revealDeaths = true
            )
        )
        val editedPlayer = rules.players.find { player -> player.id == selectedId }?.copy(
            isAlive = false
        )
        logger.info("Edited player =  $editedPlayer")
        rules.setUpPlayers(
            rules.players.map { player ->
                if (player.id == selectedId) {
                    editedPlayer!!
                } else {
                    player
                }
            }
        )

        logger.info(
            "New player =  ${rules.players.find { player -> player.id == selectedId }}"
        )


        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer


                ModeratorSleep(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = {},
                )
            }
        }


        onNodeWithText("Night Results").assertIsDisplayed()
        onNodeWithContentDescription("Killed player").assertIsDisplayed()
    }

    @Test
    fun proceed_disabledWhenPlayersAreActing() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
                round = 1
            )
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
                            when(event.intent) {
                                is ModeratorCommand.AdvancePhase -> {
                                    rules.setUpGameState(
                                        rules.gameState.value.copy(
                                            phase = event.intent.phase
                                        )
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
                    onBack = {},
                    votes = emptyList()
                )
            }
        }

        onNodeWithText("Proceed").assertIsDisplayed()
        onNodeWithText("Proceed").assertIsNotEnabled()

    }

    @Test
    fun proceed_movesToTownHall() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.SLEEP,
                round = 1
            )
        )

        val editedPlayers = rules.players.map { player ->
            val actionType = player.role?.actionType

            if (player.role?.canActInSleep == true && actionType != null) {
                player.withLastAction(rules.gameState.value.round)
            } else {
                player
            }
        }

        rules.setUpPlayers(editedPlayers)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.HandleModeratorCommand -> {
                            when(event.intent) {
                                is ModeratorCommand.AdvancePhase -> {
                                    rules.setUpGameState(
                                        rules.gameState.value.copy(
                                            phase = event.intent.phase
                                        )
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
                    onBack = {},
                    votes = emptyList()
                )
            }
        }

        onNodeWithText("Proceed").assertIsDisplayed()
        onNodeWithText("Proceed").performClick()

        waitForIdle()

        val expectedPhase = GamePhase.TOWN_HALL

        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}")
    }
}