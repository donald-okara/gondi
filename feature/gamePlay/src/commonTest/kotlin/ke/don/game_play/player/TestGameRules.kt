package ke.don.game_play.player


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
import ke.don.game_play.player.model.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.ExperimentalTime

class TestGameRules @OptIn(ExperimentalTestApi::class) constructor(
    private val rule: ComposeUiTest
) {

    // Exposed test state
    private val _gameState = MutableStateFlow(defaultGameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    @OptIn(ExperimentalTime::class)
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState


    var players: List<Player> = emptyList()
        private set

    var currentPlayer: Player = Player()
        private set

    var votes: List<Vote> = emptyList()
        private set

    @OptIn(ExperimentalTestApi::class)
    fun setContent(content: @Composable () -> Unit) = rule.setContent{
        AppTheme(
          theme = Theme.Dark,
        ){
            content()
        }
    }
    fun setupDefaults() {
        setUpPlayers(
            defaultPlayers()
        )
        setUpCurrentPlayer(
            players.find { it.id == "3" }!!
        )

        setUpGameState(
            defaultGameState()
        )
        setUpVotes(
            defaultVotes()
        )
    }

    fun setUpPlayerState(
        state: PlayerState
    ){
        _playerState.update {
            state
        }
    }

    fun setUpGameState(
        state: GameState
    ){
        _gameState.update {
            state
        }
    }

    fun setUpPlayers(
        players: List<Player>
    ){
        this.players = players
    }

    fun setUpVotes(
        votes: List<Vote>
    ){
        this.votes = votes
    }

    fun setUpCurrentPlayer(
        player: Player
    ){
        this.currentPlayer = player
    }


    private fun defaultGameState() = GameState(
        id = "123",
        name = "Test Game",
        phase = GamePhase.LOBBY,
        availableSlots = 7
    )

    private fun defaultVotes() = listOf<Vote>(
        Vote(
            voterId = "1",
            targetId = "2",
            isGuilty = true
        ),Vote(
            voterId = "3",
            targetId = "2",
            isGuilty = true
        ),Vote(
            voterId = "4",
            targetId = "2",
            isGuilty = true
        ),Vote(
            voterId = "5",
            targetId = "2",
            isGuilty = true
        ),Vote(
            voterId = "6",
            targetId = "2",
            isGuilty = true
        ),Vote(
            voterId = "7",
            targetId = "2",
            isGuilty = true
        )
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