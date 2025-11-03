package ke.don.local.db

import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote

class LocalDatabase(
    databaseFactory: DatabaseFactory
) {
    private val database = GondiDatabase(
        databaseFactory.createDriver()
    )
    private val stateQueries = database.game_stateQueries
    private val playersQueries = database.playersQueries
    private val votesQueries = database.votesQueries

    /**
     * GAME STATE
     */
    fun getAllGameState(): List<GameState> = stateQueries.getAllGAmeState()
            .executeAsList()
            .map { row ->
                row.toGameState
            }


    fun getGameState(id: String): GameState = stateQueries.getGameState(id).executeAsOne().toGameState


    fun insertOrReplaceGameState(gameState: GameState) = stateQueries.insertOrReplaceGameState(
            gameState.toGameState.id,
            gameState.toGameState.phase,
            gameState.toGameState.round,
            gameState.toGameState.pending_kills,
            gameState.toGameState.last_saved_player_id,
            gameState.toGameState.accused_player_id,
            gameState.toGameState.reveal_eliminated_player
        )


    fun updatePhase(phase: String, round: Long, id: String ) = stateQueries.updatePhase(phase, round, id)


    fun toggleRevealFlag(flag: Boolean,id: String) = stateQueries.toggleRevealFlag(booleanAdapter.encode(flag), id)


    fun clearGameState()= stateQueries.clearGameState()


    /**
     * PLAYER
     */

    fun getAllPlayers(): List<Player> = playersQueries.getAllPlayers().executeAsList().map { it.toPlayer }

    fun getAlivePlayers(): List<Player> = playersQueries.getAlivePlayers().executeAsList().map { it.toPlayer }

    fun getPlayerById(id: String): Player = playersQueries.getPlayerById(id).executeAsOne().toPlayer

    fun insertOrReplacePlayer(player: Player) = playersQueries.insertOrReplacePlayer(
        player.id,
        name = player.name,
        role = player.role?.let { roleAdapter.encode(it) },
        is_alive = booleanAdapter.encode(player.isAlive),
        known_identities = knownIdentitiesAdapter.encode(player.knownIdentities),
        last_action = player.lastAction?.let { playerActionAdapter.encode(it) },
        avatar = player.avatar?.let { avatarAdapter.encode(it) },
        background = player.background.let { backgroundAdapter.encode(it) },
    )

    fun updateAliveStatus(isAlive: Boolean, id: String) = playersQueries.updateAliveStatus(booleanAdapter.encode(isAlive), id)

    fun updateLastAction(lastAction: PlayerAction, id: String) = playersQueries.updateLastAction(playerActionAdapter.encode(lastAction), id)

    fun updateKnownIdentities(knownIdentities: Map<String, Role?>, id: String) = playersQueries.updateKnownIdentities(knownIdentitiesAdapter.encode(knownIdentities), id)

    fun clearPlayers() = playersQueries.clearPlayers()

    /**
     * VOTE
     */

    fun getAllVotes(): List<Vote> = votesQueries.getAllVotes().executeAsList().map { it.toVote }

    fun insertOrReplaceVote(voterId: String, targetId: String, isGuilty: Boolean) = votesQueries.insertOrReplaceVote(voterId, targetId, booleanAdapter.encode(isGuilty))

    fun clearVotes() = votesQueries.clearVotes()
}