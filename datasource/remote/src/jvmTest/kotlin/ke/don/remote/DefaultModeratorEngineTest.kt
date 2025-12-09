/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultModeratorEngineTest : BaseGameTest() {
    val logger = Logger("DefaultModeratorEngineTest")

    /**
     * CREATE
     */
    @Test
    fun testCreateGame_success() = runTest {
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

        val gondi = db.getPlayerById(player10.id).firstOrNull()
        assertNotNull(game)
        assert(game.phase == GamePhase.SLEEP)

        assert(gondi?.knownIdentities?.isNotEmpty() == true)
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

        val gameFirst = db.getGameState(gameState.id).firstOrNull()

        gameEngine.reduce(gameState.id, PlayerIntent.Accuse(accuser.id, gameFirst?.round ?: error("Round cannot be null"), gondi.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Second(seconder.id, gameFirst.round, gondi.id))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.COURT))

        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player3.id, gameFirst.round, Vote(voterId = player3.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player4.id, gameFirst.round, Vote(voterId = player4.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player5.id, gameFirst.round, Vote(voterId = player5.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player6.id, gameFirst.round, Vote(voterId = player6.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player7.id, gameFirst.round, Vote(voterId = player7.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player8.id, gameFirst.round, Vote(voterId = player8.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player9.id, gameFirst.round, Vote(voterId = player9.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player10.id, gameFirst.round, Vote(voterId = player10.id, targetId = player2.id, isGuilty = isGuilty)))

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

        val gameFirst = db.getGameState(gameState.id).firstOrNull()
        gameEngine.reduce(gameState.id, PlayerIntent.Accuse(accuser.id, gameFirst?.round ?: error("Round cannot be null"), gondi.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Second(seconder.id, gameFirst.round, gondi.id))

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.COURT))

        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player3.id, gameFirst.round, Vote(voterId = player3.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player4.id, gameFirst.round, Vote(voterId = player4.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player5.id, gameFirst.round, Vote(voterId = player5.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player6.id, gameFirst.round, Vote(voterId = player6.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player7.id, gameFirst.round, Vote(voterId = player7.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player8.id, gameFirst.round, Vote(voterId = player8.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player9.id, gameFirst.round, Vote(voterId = player9.id, targetId = player2.id, isGuilty = isGuilty)))
        gameEngine.reduce(gameState.id, PlayerIntent.Vote(player10.id, gameFirst.round, Vote(voterId = player10.id, targetId = player2.id, isGuilty = isGuilty)))

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

        val gameFirst = db.getGameState(gameState.id).firstOrNull()

        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi.id, gameFirst?.round ?: error("Round cannot be null"), target.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi2.id, gameFirst.round, target2.id))

        gameEngine.reduce(gameState.id, PlayerIntent.Save(doctor.id, gameFirst.round, target2.id))

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

    @Test
    fun textExoneratePlayer_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val accuser = player4
        val seconder = player5
        val accused = player6

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.TOWN_HALL))

        executeValidated(gameEngine, PlayerIntent.Accuse(accuser.id, gameState.round, accused.id))
        executeValidated(gameEngine, PlayerIntent.Second(seconder.id, gameState.round, accused.id))

        val game = db.getGameState(gameState.id).firstOrNull()
        assertEquals(accused.id, game?.accusedPlayer?.targetId)
        assertEquals(accused.id, game?.second?.targetId)

        moderatorEngine.handle(gameState.id, ModeratorCommand.ExoneratePlayer(gameState.id))

        val gameAfter = db.getGameState(gameState.id).firstOrNull()
        assertNull(gameAfter?.accusedPlayer)
        assertNull(gameAfter?.second)
    }

    /**
     * REMOVE PLAYER,
     */
    @Test
    fun removePlayer_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1
        val player = player9
        val role = Role.VILLAGER

        db.updatePlayerRole(role, player.id)
        val updated = db.getPlayerById(player.id).firstOrNull()
        assertEquals(role, updated?.role)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        db.updatePhase(GamePhase.LOBBY, 0L, gameState.id)

        moderatorEngine.handle(gameState.id, ModeratorCommand.RemovePlayer(gameState.id, player.id))

        val fetched = db.getPlayerById(player.id).firstOrNull()
        assert(fetched != null)
        assert(fetched?.isAlive == false)
        assertNull(fetched?.role)
    }

    @Test
    fun removePlayer_successWhenNotLobby() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1
        val player = player9
        val role = Role.VILLAGER

        db.updatePlayerRole(role, player.id)
        val updated = db.getPlayerById(player.id).firstOrNull()
        assertEquals(role, updated?.role)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        db.updatePhase(GamePhase.SLEEP, 0L, gameState.id)

        moderatorEngine.handle(gameState.id, ModeratorCommand.RemovePlayer(gameState.id, player.id))

        val fetched = db.getPlayerById(player.id).firstOrNull()
        assert(fetched != null)
        assert(fetched?.isAlive == false)
        assertNotNull(fetched?.role)
        assertEquals(role, fetched.role)
    }

    /**
     * START GAME
     *
     * * These commands directly call [db] methods that have been
     * tested in other classes. We will skip them
     */

    /**
     * GAME OVER
     *
     */

    @Test
    fun revealDeath_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        moderatorEngine.handle(gameState.id, ModeratorCommand.RevealDeaths(gameState.id))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assertTrue(game.revealEliminatedPlayer)
    }

    @Test
    fun testGameOver_success() = runTest {
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
    fun testGameOver_gondisWin() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        db.transaction {
            db.updateAliveStatus(false, player2.id, gameState.round)
            db.updateAliveStatus(false, player3.id, gameState.round)
            db.updateAliveStatus(false, player4.id, gameState.round)
            db.updateAliveStatus(false, player5.id, gameState.round)
            db.updateAliveStatus(false, player6.id, gameState.round)
            db.updateAliveStatus(false, player7.id, gameState.round)
        }
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.GAME_OVER)
        assert(game.winners == Faction.GONDI)
    }

    @Test
    fun testGameOver_villagersWin() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)

        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        db.transaction {
            db.updateAliveStatus(false, player9.id, gameState.round)
            db.updateAliveStatus(false, player10.id, gameState.round)
        }
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        val game = db.getGameState(gameState.id).firstOrNull()

        assertNotNull(game)
        assert(game.phase == GamePhase.GAME_OVER)
        assert(game.winners == Faction.VILLAGER)
    }

    /**
     * LOBBY
     */

    @Test
    fun testLobby_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val moderator = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)
        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))

        // All players should have roles before advancing to lobby
        val playersBefore = db.getAllPlayers().firstOrNull()
        assertNotNull(playersBefore)
        assertTrue(playersBefore.all { it.role != null })

        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.LOBBY))

        val game = db.getGameState(gameState.id).firstOrNull()
        assertNotNull(game)
        assert(game.phase == GamePhase.LOBBY)

        // Only the moderator should have a role in the lobby
        val playersAfter = db.getAllPlayers().firstOrNull()
        assertNotNull(playersAfter)
        assertEquals(playersAfter.filter { it.role != null }.size, 1)
        assertEquals(playersAfter.first { it.role != null }.role, Role.MODERATOR)
    }

    /**
     * RESET GAME
     *
     */

    @Test
    fun testLeaveGame_success() = runTest {
        val moderatorEngine = DefaultModeratorEngine(db)
        val gameEngine = DefaultGameEngine(db)

        val moderator = player1
        val target = player2

        db.batchUpdatePlayerRole(batchUpdateRoles)

        moderatorEngine.handle(gameState.id, ModeratorCommand.CreateGame(gameState.id, gameState, moderator))
        moderatorEngine.handle(gameState.id, ModeratorCommand.AdvancePhase(gameState.id, GamePhase.SLEEP))

        executeValidated(engine = gameEngine, intent = PlayerIntent.Leave(target.id, gameState.round))
        val game = db.getGameState(gameState.id).firstOrNull()
        val player = db.getPlayerById(target.id).firstOrNull()

        assertNotNull(game)
        assert(player?.isAlive == false)
    }

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

        val gameFirst = db.getGameState(gameState.id).firstOrNull()
        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi.id, gameFirst?.round ?: error("Round cannot be null"), target.id))
        gameEngine.reduce(gameState.id, PlayerIntent.Kill(gondi2.id, gameFirst.round, target2.id))

        gameEngine.reduce(gameState.id, PlayerIntent.Save(doctor.id, gameFirst.round, target2.id))

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
