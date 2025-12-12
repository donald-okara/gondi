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
import ke.don.game_play.moderator.screens.ModeratorTownHall
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.screens.PlayerTownHall
import ke.don.utils.Logger
import ke.don.utils.capitaliseFirst
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
        rules.setUpDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                pendingKills = listOf(selectedId),
                lastSavedPlayerId = selectedId
            )
        )
        rules.setUpPlayerState(
            rules.playerState.value.copy(
                revealDeaths = true
            )
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer


                PlayerTownHall(
                    gameState = gameState,
                    myPlayer = currentPlayer,
                    players = players,
                    playerState = playerState,
                    onEvent = {},
                )
            }
        }


        onNodeWithText("Night Results").assertIsDisplayed()
        onNodeWithContentDescription("Saved player").assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun selectPlayer_showsModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setUpDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL
            )
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler){
                    when(event) {
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
        rules.setUpDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL
            )
        )

        rules.setUpPlayerState(
            rules.playerState.value.copy(
                selectedId = selectedId
            )
        )
        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler){
                    when(event) {
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

        onNodeWithText("Accuse ${selectedPlayer?.name} and put them on trial? Are you sure?").assertIsDisplayed()
        onNodeWithText("I am sure").performClick()

        waitForIdle()

        assertTrue(clicked.value)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun secondPlayer_setsSecond() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setUpDefaults()

        val selectedId = "2"
        val clicked = mutableStateOf(false)

        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.TOWN_HALL,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = "5"
                )
            )
        )

        rules.setContent {
            WithTestLifecycle {
                val gameState by rules.gameState.collectAsState()
                val playerState by rules.playerState.collectAsState()
                val players = rules.players
                val currentPlayer = rules.currentPlayer

                fun onEvent(event: PlayerHandler){
                    when(event) {
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

        onNodeWithText("Waiting for a second to proceed to court").assertIsDisplayed()
        onNodeWithText("Second The Accusation").assertIsDisplayed()
        onNodeWithText("Second The Accusation").performClick()

        waitForIdle()

        assertTrue(clicked.value)
    }
}