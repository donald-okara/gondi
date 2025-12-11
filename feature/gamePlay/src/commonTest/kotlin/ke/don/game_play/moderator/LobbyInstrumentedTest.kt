package ke.don.game_play.moderator

import WithTestLifecycle
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.components.list_items.GamePhases
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.moderator.screens.MainModeratorContent
import ke.don.game_play.moderator.useCases.PLAYER_LOWER_LIMIT
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class LobbyInstrumentedTest {
    val logger = Logger("LobbyInstrumentedTest")

    @Test
    fun advancePhaseMovesToSleep_SuccessWhenPlayersAreEnough() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        ModeratorHandler.StartGame -> {
                            clicked.value = true
                            rules.setUpGameState(rules.gameState.value.copy(phase = GamePhase.SLEEP))

                            logger.info(
                                "State Phase: ${gameState.phase.name.capitaliseFirst()} Live Phase: ${rules.gameState.value.phase.name.capitaliseFirst()}"
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
        val expectedButtonText = "Start Game"

        onNodeWithText(expectedButtonText).assertIsDisplayed()
        onNodeWithText(expectedButtonText).assertIsEnabled()
        onNodeWithText(expectedButtonText).performClick()

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
            rules.players.take(rules.gameState.value.availableSlots.toInt() - 1)
        )
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        ModeratorHandler.StartGame -> {
                            clicked.value = true
                            rules.setUpGameState(rules.gameState.value.copy(phase = GamePhase.SLEEP))

                            logger.info(
                                "State Phase: ${gameState.phase.name.capitaliseFirst()} Live Phase: ${rules.gameState.value.phase.name.capitaliseFirst()}"
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
        val expectedButtonText = "Start with ${rules.players.size} players"

        onNodeWithText(expectedButtonText).assertIsDisplayed()
        onNodeWithText(expectedButtonText).assertIsEnabled()
        onNodeWithText(expectedButtonText).performClick()

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
            rules.players.take(PLAYER_LOWER_LIMIT - 1)
        )
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        ModeratorHandler.StartGame -> {
                            clicked.value = true
                            rules.setUpGameState(rules.gameState.value.copy(phase = GamePhase.SLEEP))

                            logger.info(
                                "State Phase: ${gameState.phase.name.capitaliseFirst()} Live Phase: ${rules.gameState.value.phase.name.capitaliseFirst()}"
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

        val expectedButtonText = "Start with ${rules.players.size} players"

        onNodeWithText(expectedButtonText).assertIsDisplayed()
        onNodeWithText(expectedButtonText).assertIsNotEnabled()
    }
}