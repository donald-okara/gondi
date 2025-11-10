package ke.don.remote

import app.cash.sqldelight.db.SqlDriver
import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.local.db.DatabaseFactory
import ke.don.local.db.GondiDatabase
import ke.don.local.db.JVMDatabaseFactory
import ke.don.local.db.LocalDatabase
import ke.don.remote.gameplay.validateIntent
import ke.don.utils.Logger
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

abstract class BaseGameTest {

    lateinit var db: LocalDatabase
    val gameState = GameState(
        id = "game1",
        phase = GamePhase.LOBBY,
        round = 0L,
        winners = null,
        pendingKills = emptyList(),
        lastSavedPlayerId = null,
        accusedPlayer = null,
        second = null,
        revealEliminatedPlayer = false
    )

    @Before
    fun setupDb() = runBlocking {
        db = createDb()
        prepopulateDb()
    }

    @After
    fun teardownDb() {
        db.clearGameState()
        db.clearVotes()
        db.clearPlayers()
    }


    private fun prepopulateDb() {
        db.insertOrReplacePlayer(
            Player(
                id = "player1",
                role = Role.VILLAGER,
                isAlive = true,
                lastAction = null,
                knownIdentities = emptyList(),
                avatar = Avatar.Christian,
                name = "Otis",
                timeOfDeath = null,
                background = AvatarBackground.GREEN_EMERALD
            )
        )
        db.insertOrReplacePlayer(
            Player(
                id = "player2",
                role = Role.GONDI,
                isAlive = true,
                lastAction = null,
                knownIdentities = emptyList(),
                avatar = Avatar.Leo,
                name = "Noah",
                timeOfDeath = null,
                background = AvatarBackground.PURPLE_ORCHID
            )
        )
        db.insertOrReplaceGameState( gameState )
    }

    fun createDb(): LocalDatabase {
        val driver = JVMDatabaseFactory().createInMemoryDriver() // your helper for in-memory SQLDelight
        return LocalDatabase(object : DatabaseFactory {
            override fun createDriver() = driver
        })
    }

    suspend fun executeValidated(
        engine: GameEngine,
        intent: PlayerIntent,
        currentPhase: GamePhase
    ) {
        val logger = Logger("executeValidated")
        if (validateIntent(db, gameState.id, intent, currentPhase)) {
            engine.reduce(gameState.id, intent)
        } else {
            val error = "Invalid intent for player ${intent.playerId} in phase $currentPhase"
            logger.error(error)
            throw IllegalArgumentException(error)
        }
    }
}
