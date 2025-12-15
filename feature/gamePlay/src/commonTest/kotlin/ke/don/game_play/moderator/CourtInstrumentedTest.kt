package ke.don.game_play.moderator

import WithTestLifecycle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.state.GamePhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.game_play.moderator.screens.ModeratorCourt
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.screens.PlayerCourt
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime


/**
 * Instrumented UI tests for the [GamePhase.COURT] screen.
 * This class verifies the behavior and state of the court UI from the moderator's perspective,
 * particularly focusing on the "Proceed" button's state based on player votes.
 */
@OptIn(ExperimentalTestApi::class)
class CourtInstrumentedTest {
    val logger = Logger("CourtInstrumentedTest")

    @ExperimentalTime
    @Test
    fun proceed_isPresentAndDisabledWhenVotesAreEmpty() = runComposeUiTest {
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
                second = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                )
            )
        )
        val selectedPhase = mutableStateOf(GamePhase.COURT)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event : ModeratorHandler){
                    when(event){
                        is ModeratorHandler.HandleModeratorCommand -> {
                            selectedPhase.value = GamePhase.SLEEP
                        }
                        else -> {}
                    }
                }
                ModeratorCourt(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    moderatorState = moderatorState,
                    onEvent = ::onEvent,
                    votes = emptyList()
                )
            }
        }

        onNodeWithText("Proceed").assertIsDisplayed()
        onNodeWithText("Proceed").assertIsNotEnabled()
    }
    @ExperimentalTime
    @Test
    fun proceed_isPresentAndEnabledWhenVotesNotEmpty() = runComposeUiTest {
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
                second = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                )
            )
        )
        val selectedPhase = mutableStateOf(GamePhase.COURT)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()
                val players = rules.players
                val votes = rules.votes
                val currentPlayer = rules.currentPlayer

                fun onEvent(event : ModeratorHandler){
                    when(event){
                        is ModeratorHandler.HandleModeratorCommand -> {
                            selectedPhase.value = GamePhase.SLEEP
                            rules.setUpGameState(
                                gameState.copy(
                                    phase = selectedPhase.value
                                )
                            )
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
                    votes = votes,
                    onBack = {}
                )
            }
        }

        onNodeWithText("Proceed").assertIsDisplayed()
        onNodeWithText("Proceed").assertIsEnabled()
        onNodeWithText("Proceed").performClick()

        waitForIdle()

        val expectedPhase = GamePhase.SLEEP
        assertEquals(expectedPhase, selectedPhase.value)
        onNodeWithText(
            "${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}"
        ).assertIsDisplayed()
    }
}


/**
 * TODO()
 * Test cases for *Game Over*
 * * "Play again" is *displayed* for moderator
 * * Winning faction name is displayed
 * * "Play again" moves to sleep
 */