package ke.don.game_play.moderator.useCases

import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.local.datastore.ProfileStore
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameSessionState(
    private val database: LocalDatabase,
    private val profileStore: ProfileStore
) {
    val gameState = MutableStateFlow<GameState?>(null)
    val players = MutableStateFlow<List<Player>>(emptyList())
    val votes = MutableStateFlow<List<Vote>>(emptyList())
    val moderatorState = MutableStateFlow(ModeratorState())
    val hostPlayer = profileStore.profileFlow.map { it?.toPlayer() }

    private var dbObserveJob: Job? = null

    fun updateModeratorState(transform: (ModeratorState) -> ModeratorState) {
        moderatorState.update {
            transform(moderatorState.value)
        }
    }

    fun observe(gameId: String, scope: CoroutineScope) {
        dbObserveJob?.cancel()

        dbObserveJob = scope.launch {
            combine(
                database.getGameState(gameId),
                database.getAllPlayers(),
                database.getAllVotes(),
            ) { state, players, votes ->
                Triple(state, players, votes)
            }.collect { (state, playerList, voteList) ->
                gameState.value = state
                players.value = playerList
                votes.value = voteList
            }
        }
    }
    fun stopObserving() {
        dbObserveJob?.cancel()

        val cleanupScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        cleanupScope.launch {
            moderatorState.update {
                ModeratorState()
            }
            gameState.value = null
            players.value = emptyList()
            votes.value = emptyList()
        }
    }
}