package ke.don.remote

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Vote
import ke.don.remote.server.DefaultGameEngine
import ke.don.remote.server.DefaultModeratorEngine
import ke.don.utils.Logger
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertNotNull

class DefaultModeratorEngineTest: BaseGameTest() {
    val logger = Logger("DefaultModeratorEngineTest")

    /**
     * CREATE
     */
    @Test
    fun testCreateGame_success() = runTest{
        val engine = DefaultModeratorEngine(db)
        val moderator = player1
        engine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        val game = db.getGameState(gameState.id).firstOrNull()
        val players = db.getAllPlayers().firstOrNull()

        assertNotNull(game)
        assert(game.name == gameState.name)
        assert(players?.contains(moderator.copy(role = Role.MODERATOR)) == true)
    }

    /**
     * ASSIGN ROLES
     *
     * * These commands directly call [db] methods that have been
     * tested in other classes. We will skip them
     */

    /**
     * PHASE ADVANCEMENT
     */

    /**
     * SLEEP
     */
    @Test
    fun testMoveToSleep_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))


        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.SLEEP)
    }

    @Test
    fun testMoveToSleep_killsGondiWhenOutvoted() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val gondi = player2
        val accuser = player4
        val seconder = player4
        val isGuilty = true

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))

        gameEngine.reduce(gameState.id, PlayerIntent.Accuse(accuser.id, gondi.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Second(seconder.id, gondi.id))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.COURT))

        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player3.id, Vote(voterId = player3.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player4.id, Vote(voterId = player4.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player5.id, Vote(voterId = player5.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player6.id, Vote(voterId = player6.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player7.id, Vote(voterId = player7.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player8.id, Vote(voterId = player8.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player9.id, Vote(voterId = player9.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player10.id, Vote(voterId = player10.id, targetId = player2.id, isGuilty = isGuilty)))


        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))


        val game = db.getGameState(gameState.id).firstOrNull()
        val player = db.getPlayerById(gondi.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.SLEEP)
        assert(player?.isAlive == false)
    }

    @Test
    fun testMoveToSleep_doesntKillGondiWhenNotOutvoted() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val gondi = player2
        val accuser = player4
        val seconder = player4
        val isGuilty = false

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))

        gameEngine.reduce(gameState.id, PlayerIntent.Accuse(accuser.id, gondi.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Second(seconder.id, gondi.id))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.COURT))

        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player3.id, Vote(voterId = player3.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player4.id, Vote(voterId = player4.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player5.id, Vote(voterId = player5.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player6.id, Vote(voterId = player6.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player7.id, Vote(voterId = player7.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player8.id, Vote(voterId = player8.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player9.id, Vote(voterId = player9.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player10.id, Vote(voterId = player10.id, targetId = player2.id, isGuilty = isGuilty)))


        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))


        val game = db.getGameState(gameState.id).firstOrNull()
        val player = db.getPlayerById(gondi.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.SLEEP)
        assert(player?.isAlive == true)
    }


    /**
     * BOOM! Just tested court as well
     */

    /**
     * TOWN HALL
     */
    @Test
    fun testMoveToTownHall_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))
        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.TOWN_HALL)
    }

    @Test
    fun testMoveToTownHall_successWhenPlayersKilledAndSaved() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val gondi = player9
        val gondi2 = player10
        val target = player2
        val target2 = player3
        val doctor = player6

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi.id, target.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi2.id, target2.id))

        gameEngine.reduce(gameState.id, PlayerIntent.Save(doctor.id, target2.id))

        val gameAsleep = db.getGameState(gameState.id).firstOrNull()

        assert(gameAsleep?.pendingKills?.contains(target.id) == true)
        assert(gameAsleep?.pendingKills?.contains(target2.id) == true)
        assert(gameAsleep?.lastSavedPlayerId == target2.id)

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))
        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.TOWN_HALL)

        val targetTownHall = db.getPlayerById(target.id).firstOrNull()
        val target2TownHall = db.getPlayerById(target2.id).firstOrNull()
        assert(targetTownHall?.isAlive == false)
        assert(target2TownHall?.isAlive == true)
    }


    /**
     * REMOVE PLAYER, REVEAL DEATHS, START GAME
     *
     * * These commands directly call [db] methods that have been
     * tested in other classes. We will skip them
     */


    /**
     * GAME OVER
     *
     */
    @Test
    fun testGameOver_success() = runTest{
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        moderatorEngine.handle(gameState.id, ModeratorCommand.GameOver(gameState.id, Faction.VILLAGER))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.GAME_OVER)
        assert(game.winners == Faction.VILLAGER)
    }

    @Test
    fun testGameOver_gondisWin() = runTest{
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        db.transaction {
            db.updateAliveStatus(false, player2.id)
            db.updateAliveStatus(false, player3.id)
            db.updateAliveStatus(false, player4.id)
            db.updateAliveStatus(false, player5.id)
            db.updateAliveStatus(false, player6.id)
            db.updateAliveStatus(false, player7.id)
        }
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.GAME_OVER)
        assert(game.winners == Faction.GONDI)
    }

    @Test
    fun testGameOver_vilagersWin() = runTest{
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        db.transaction {
            db.updateAliveStatus(false, player9.id)
            db.updateAliveStatus(false, player10.id)
        }
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.GAME_OVER)
        assert(game.winners == Faction.VILLAGER)
    }

    /**
     * RESET GAME
     *
     */

    @Test
    fun testResetGame_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val gondi = player9
        val gondi2 = player10
        val target = player2
        val target2 = player3
        val doctor = player6

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi.id, target.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi2.id, target2.id))

        gameEngine.reduce(gameState.id, PlayerIntent.Save(doctor.id, target2.id))

        val gameAsleep = db.getGameState(gameState.id).firstOrNull()

        assert(gameAsleep?.pendingKills?.contains(target.id) == true)
        assert(gameAsleep?.pendingKills?.contains(target2.id) == true)
        assert(gameAsleep?.lastSavedPlayerId == target2.id)

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))
        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.TOWN_HALL)

        val targetTownHall = db.getPlayerById(target.id).firstOrNull()
        val target2TownHall = db.getPlayerById(target2.id).firstOrNull()
        assert(targetTownHall?.isAlive == false)
        assert(target2TownHall?.isAlive == true)

        moderatorEngine.handle(gameState.id, ModeratorCommand.ResetGame(gameState.id))

        val games = db.getAllGameState().firstOrNull()
        val votes = db.getAllVotes().firstOrNull()
        val players = db.getAllPlayers().firstOrNull()

        assert(games?.isEmpty() == true)
        assert(votes?.isEmpty() == true)
        assert(players?.isEmpty() == true)

    }


}