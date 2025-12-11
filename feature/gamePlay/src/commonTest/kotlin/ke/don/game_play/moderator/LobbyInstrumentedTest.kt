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
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class LobbyInstrumentedTest {
    val logger = Logger("LobbyInstrumentedTest")

    @Test
    fun advancePhaseMovesToSleep() = runComposeUiTest {
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
        onNodeWithText("Start Game").assertIsDisplayed()
        onNodeWithText("Start Game").assertIsEnabled()
        onNodeWithText("Start Game").performClick()

        waitForIdle()
        val expectedPhase = GamePhase.SLEEP

        assertEquals(true, clicked.value)
        assertEquals(expectedPhase, rules.gameState.value.phase)
        onNodeWithText("${expectedPhase.name.capitaliseFirst()} for ${rules.gameState.value.name}").assertIsDisplayed()
    }
}