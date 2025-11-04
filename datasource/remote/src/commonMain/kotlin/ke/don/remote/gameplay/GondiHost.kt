package ke.don.remote.gameplay

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GondiHost(
    private val server: LocalServer,
    private val database: LocalDatabase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _votes = MutableStateFlow<List<Vote>>(emptyList())
    val votes: StateFlow<List<Vote>> = _votes

    init {
        observeDatabase()
    }

    private fun observeDatabase() {
        scope.launch {
            database.getFirstGameState().collect { rows ->
                _gameState.value = rows
            }
        }

        scope.launch {
            database.getAllPlayers().collect { rows ->
                _players.value = rows
            }
        }

        scope.launch {
            database.getAllVotes().collect { rows ->
                _votes.value = rows
            }
        }
    }

    // Moderator actions
    suspend fun handleIntent(intent: ModeratorCommand) = server.handleModeratorCommand(intent)
}
