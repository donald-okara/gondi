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

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class CourtInstrumentedTest {
    val logger = Logger("TownHallInstrumentedTest")

    @Test
    fun vote_showsVoteModal() = runComposeUiTest {
        val rules = TestGameRules(this)
        rules.setUpDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                )
            )
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
                                    showVote = true
                                )
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
                    votes = votes
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
        rules.setUpDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                )
            )
        )
        val editedVotes = rules.votes.minus(
            rules.votes.find { vote -> vote.voterId == rules.currentPlayer.id }!!
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
                                    showVote = true
                                )
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
                    votes = votes
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
        rules.setUpDefaults()
        val selectedId = "2"
        rules.setUpGameState(
            rules.gameState.value.copy(
                phase = GamePhase.COURT,
                accusedPlayer = PlayerAction(
                    type = ActionType.ACCUSE,
                    round = rules.gameState.value.round,
                    targetId = selectedId,
                    playerId = rules.currentPlayer.id,
                )
            )
        )
        val editedVotes = rules.votes.minus(
            rules.votes.find { vote -> vote.voterId == rules.currentPlayer.id }!!
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
            "Edited player: ${editedPlayers.find{it.id == rules.currentPlayer.id}}"
        )
        logger.info(
            "Current player: ${rules.currentPlayer}"
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
                                    showVote = true
                                )
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
                    votes = votes
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