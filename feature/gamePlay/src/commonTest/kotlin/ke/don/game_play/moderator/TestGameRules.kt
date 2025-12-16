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

import androidx.compose.runtime.*
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import ke.don.design.theme.AppTheme
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.moderator.model.ModeratorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.ExperimentalTime

/**
 * A test rule helper class for managing game state and setting up the UI environment
 * for Jetpack Compose UI tests.
 *
 * This class encapsulates common setup logic for tests related to the game's UI. It provides
 * default states for the game, players, and votes, and offers methods to customize these states
 * for specific test scenarios. It also handles wrapping the test content in the required `AppTheme`.
 *
 * This is designed to be used with a [ComposeUiTest] rule, such as `createComposeRule()`.
 *
 * Example Usage:
 * ```
 * @get:Rule
 * val composeTestRule = createComposeRule()
 *
 * private lateinit var testGameRules: TestGameRules
 *
 * @Before
 * fun setUp() {
 *     testGameRules = TestGameRules(composeTestRule)
 *     testGameRules.setupDefaults() // Initialize with default test data
 * }
 *
 * @Test
 * fun myUiTest() {
 *     testGameRules.setContent {
 *         // Your Composable under test, using state from testGameRules
 *         MyScreen(state = testGameRules.moderatorState.collectAsState().value)
 *     }
 *
 *     // ... perform assertions
 * }
 * ```
 *
 * @param rule The [ComposeUiTest] instance used to set the content for the UI test.
 */
class TestGameRules
@OptIn(ExperimentalTestApi::class)
constructor(
    private val rule: ComposeUiTest,
) {

    // Exposed test state
    private val _gameState = MutableStateFlow(defaultGameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _moderatorState = MutableStateFlow(defaultModeratorState())
    val moderatorState: StateFlow<ModeratorState> = _moderatorState

    var players: List<Player> = emptyList()
        private set

    var currentPlayer: Player = Player()
        private set

    var votes: List<Vote> = emptyList()
        private set

    @OptIn(ExperimentalTestApi::class)
    fun setContent(content: @Composable () -> Unit) = rule.setContent {
        AppTheme(
            theme = Theme.Dark,
        ) {
            content()
        }
    }

    fun setupDefaults() {
        setUpPlayers(
            defaultPlayers(),
        )
        setUpCurrentPlayer(
            players.first { it.role == Role.MODERATOR },
        )

        setUpGameState(
            defaultGameState(),
        )

        setUpModeratorState(
            defaultModeratorState(gameState.value),
        )
        setUpVotes(
            defaultVotes(),
        )
    }

    fun setUpModeratorState(
        state: ModeratorState,
    ) {
        _moderatorState.update {
            state
        }
    }

    fun setUpGameState(
        state: GameState,
    ) {
        _gameState.update {
            state
        }
    }

    fun setUpPlayers(
        players: List<Player>,
    ) {
        this.players = players
    }

    fun setUpVotes(
        votes: List<Vote>,
    ) {
        this.votes = votes
    }

    fun setUpCurrentPlayer(
        player: Player,
    ) {
        this.currentPlayer = player
    }

    private fun defaultGameState() = GameState(
        id = "123",
        name = "Test Game",
        phase = GamePhase.LOBBY,
        availableSlots = 7,
    )

    @OptIn(ExperimentalTime::class)
    private fun defaultModeratorState(newGame: GameState? = null) = ModeratorState(
        newGame = newGame ?: defaultGameState(),
    )

    private fun defaultVotes() = listOf<Vote>(
        Vote(
            voterId = "1",
            targetId = "2",
            isGuilty = true,
        ),
        Vote(
            voterId = "3",
            targetId = "2",
            isGuilty = true,
        ),
        Vote(
            voterId = "4",
            targetId = "2",
            isGuilty = true,
        ),
        Vote(
            voterId = "5",
            targetId = "2",
            isGuilty = true,
        ),
        Vote(
            voterId = "6",
            targetId = "2",
            isGuilty = true,
        ),
        Vote(
            voterId = "7",
            targetId = "2",
            isGuilty = true,
        ),
    )

    private fun defaultPlayers() = listOf(
        Player(id = "1", name = "Matt Foley", role = Role.VILLAGER, Avatar.Alexander, background = AvatarBackground.PURPLE_LILAC),
        Player(id = "2", name = "Stefon Zelesky", role = Role.VILLAGER, Avatar.Christian, background = AvatarBackground.PINK_HOT),
        Player(id = "3", name = "David S. Pumpkins", role = Role.GONDI, Avatar.Amaya, background = AvatarBackground.YELLOW_BANANA),
        Player(id = "4", name = "Roseanne Roseannadanna", role = Role.DETECTIVE, Avatar.Aidan, background = AvatarBackground.GREEN_LEAFY),
        Player(id = "5", name = "Todd O'Connor", role = Role.VILLAGER, Avatar.Kimberly, background = AvatarBackground.ORANGE_CORAL),
        Player(id = "6", name = "Pat O'Neill", role = Role.VILLAGER, Avatar.George, background = AvatarBackground.PURPLE_AMETHYST),
        Player(id = "7", name = "Hans", role = Role.VILLAGER, Avatar.Jocelyn, background = AvatarBackground.GREEN_MINTY),
        Player(id = "8", name = "Franz", role = Role.MODERATOR, Avatar.Jameson, background = AvatarBackground.YELLOW_GOLDEN),
    )
}
