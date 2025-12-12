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
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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

    @Test
    fun onSelectPlayer_displaysModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)
        val roleAssigned = mutableStateOf(false)
        val removed = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.SelectPlayer -> {
                            clicked.value = true
                            rules.setUpModeratorState(
                                rules.moderatorState.value.copy(
                                    selectedPlayerId = event.id
                                )
                            )

                            logger.info(
                                "State Selected Id: ${moderatorState.selectedPlayerId} Live Selected Id: ${rules.moderatorState.value.selectedPlayerId}"
                            )
                        }

                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AssignRole -> {
                                    roleAssigned.value = true
                                }

                                is ModeratorCommand.RemovePlayer -> {
                                    removed.value = true
                                }

                                else -> {}
                            }
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
        val assignText = "Assign Role"
        val removeText = "Remove"

        onNodeWithText(playerButtonText).assertIsDisplayed()
        onNodeWithText(playerButtonText).performClick()

        waitForIdle()

        assertEquals(true, clicked.value)
        onNodeWithText(assignText).assertIsDisplayed()
        onNodeWithText(removeText).assertIsDisplayed()
    }

    @Test
    fun assign_assignDismissesModal() = runComposeUiTest {
        val selectedId = "1"
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                selectedPlayerId = selectedId
            )
        )
        val clicked = mutableStateOf(false)
        val roleAssigned = mutableStateOf(false)
        val removed = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.SelectPlayer -> {
                            clicked.value = true
                            rules.setUpModeratorState(
                                rules.moderatorState.value.copy(
                                    selectedPlayerId = event.id
                                )
                            )

                            logger.info(
                                "State Selected Id: ${moderatorState.selectedPlayerId} Live Selected Id: ${rules.moderatorState.value.selectedPlayerId}"
                            )
                        }

                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AssignRole -> {
                                    roleAssigned.value = true
                                }

                                is ModeratorCommand.RemovePlayer -> {
                                    removed.value = true
                                }

                                else -> {}
                            }
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

        val assignText = "Assign Role"
        val removeText = "Remove"
        val roleText = Role.GONDI.name.capitaliseFirst()

        onNodeWithText(assignText).assertIsDisplayed()
        onNodeWithText(removeText).assertIsDisplayed()

        onNodeWithText(assignText).performClick()
        onNodeWithText(roleText).assertIsDisplayed()
        onNodeWithText(roleText).performClick()

        waitForIdle()

        assertNull(rules.moderatorState.value.selectedPlayerId)
        assertEquals(true, roleAssigned.value)
    }

    @Test
    fun assign_removeDismissesModal() = runComposeUiTest {
        val selectedId = "1"
        val rules = TestGameRules(this)
        rules.setupDefaults()
        rules.setUpModeratorState(
            rules.moderatorState.value.copy(
                selectedPlayerId = selectedId
            )
        )
        val clicked = mutableStateOf(false)
        val roleAssigned = mutableStateOf(false)
        val removed = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

                fun onEvent(event: ModeratorHandler) {
                    when (event) {
                        is ModeratorHandler.SelectPlayer -> {
                            clicked.value = true
                            rules.setUpModeratorState(
                                rules.moderatorState.value.copy(
                                    selectedPlayerId = event.id
                                )
                            )

                            logger.info(
                                "State Selected Id: ${moderatorState.selectedPlayerId} Live Selected Id: ${rules.moderatorState.value.selectedPlayerId}"
                            )
                        }

                        is ModeratorHandler.HandleModeratorCommand -> {
                            when (event.intent) {
                                is ModeratorCommand.AssignRole -> {
                                    roleAssigned.value = true
                                }

                                is ModeratorCommand.RemovePlayer -> {
                                    removed.value = true
                                }

                                else -> {}
                            }
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

        val assignText = "Assign Role"
        val removeText = "Remove"
        val confirmationText = "I am sure"

        onNodeWithText(assignText).assertIsDisplayed()
        onNodeWithText(removeText).assertIsDisplayed()

        onNodeWithText(removeText).performClick()
        onNodeWithText(confirmationText).assertIsDisplayed()
        onNodeWithText(confirmationText).performClick()

        waitForIdle()

        assertNull(rules.moderatorState.value.selectedPlayerId)
        assertEquals(true, removed.value)
    }

    @Test
    fun onShowRules_displaysModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setupDefaults()
        val clicked = mutableStateOf(false)

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val moderatorState by rules.moderatorState.collectAsState()

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

        val showRules = "Show rules"

        onNodeWithContentDescription(showRules).assertIsDisplayed()
        onNodeWithContentDescription(showRules).performClick()

        waitForIdle()
        val gameObjectiveText = "Game Objective"

        assertEquals(true, clicked.value)
        onNodeWithText(gameObjectiveText).assertIsDisplayed()
    }
}