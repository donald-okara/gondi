/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local

import junit.framework.TestCase.assertFalse
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.local.db.DatabaseFactory
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalDatabaseTest {

    private fun createDb(): LocalDatabase {
        val driver = createInMemoryDriver() // your helper for in-memory SQLDelight
        return LocalDatabase(object : DatabaseFactory {
            override fun createDriver() = driver
        })
    }

    /** -------------------- GAMESTATE -------------------- **/

    @Test
    fun testInsertAndGetGameState_success() = runTest {
        val db = createDb()

        val state = GameState(
            id = "current",
            phase = GamePhase.SLEEP,
            round = 1,
            pendingKills = listOf("p1", "p2"),
            lastSavedPlayerId = "p3",
            accusedPlayerId = null,
            revealEliminatedPlayer = false,
        )

        db.insertOrReplaceGameState(state)

        val fetched = db.getGameState("current").first()
        assertEquals(state, fetched)
    }

    @Test
    fun testGetGameState_notExist_returnsNull() = runTest {
        val db = createDb()

        val fetched = db.getGameState("missing")
            .map { it }
            .firstOrNull()

        assertNull(fetched)
    }

    @Test
    fun testGameStateBoundary_emptyPendingKills() = runTest {
        val db = createDb()

        val state = GameState(
            id = "boundary",
            phase = GamePhase.TOWN_HALL,
            round = 0,
            pendingKills = emptyList(),
            lastSavedPlayerId = null,
            accusedPlayerId = null,
            revealEliminatedPlayer = true,
        )

        db.insertOrReplaceGameState(state)
        val fetched = db.getGameState("boundary").first()
        assertTrue(fetched?.pendingKills?.isEmpty() == true)
        assertEquals(0, fetched.round)
    }

    @Test
    fun testUpdatePhase_updatesGameState() = runTest {
        val db = createDb()
        val state = GameState(
            id = "current",
            phase = GamePhase.SLEEP,
            round = 1,
            pendingKills = emptyList(),
            lastSavedPlayerId = null,
            accusedPlayerId = null,
            revealEliminatedPlayer = false,
        )
        db.insertOrReplaceGameState(state)

        db.updatePhase(GamePhase.COURT.name, 5, "current")
        val fetched = db.getGameState("current").first()
        assertEquals(GamePhase.COURT, fetched?.phase)
        assertEquals(5, fetched?.round)
    }

    @Test
    fun testToggleRevealFlag_togglesFlag() = runTest {
        val db = createDb()
        val state = GameState(
            id = "current",
            phase = GamePhase.SLEEP,
            round = 1,
            pendingKills = emptyList(),
            lastSavedPlayerId = null,
            accusedPlayerId = null,
            revealEliminatedPlayer = false,
        )
        db.insertOrReplaceGameState(state)

        db.toggleRevealFlag(true, "current")
        var fetched = db.getGameState("current").first()
        assertTrue(fetched?.revealEliminatedPlayer == true)

        db.toggleRevealFlag(false, "current")
        fetched = db.getGameState("current").first()
        assertFalse(fetched?.revealEliminatedPlayer == true)
    }

    @Test
    fun testClearGameState_removesAllStates() = runTest {
        val db = createDb()
        val state1 = GameState(
            id = "s1",
            phase = GamePhase.SLEEP,
            round = 1,
            pendingKills = emptyList(),
            lastSavedPlayerId = null,
            accusedPlayerId = null,
            revealEliminatedPlayer = false,
        )
        val state2 = GameState(
            id = "s2",
            phase = GamePhase.COURT,
            round = 2,
            pendingKills = emptyList(),
            lastSavedPlayerId = null,
            accusedPlayerId = null,
            revealEliminatedPlayer = false,
        )

        db.insertOrReplaceGameState(state1)
        db.insertOrReplaceGameState(state2)

        var allStates = db.getAllGameState().first()
        assertEquals(2, allStates.size)

        db.clearGameState()
        allStates = db.getAllGameState().first()
        assertTrue(allStates.isEmpty())
    }

    /** -------------------- PLAYER -------------------- **/

    @Test
    fun testInsertAndGetPlayer_success() = runTest {
        val db = createDb()

        val player = Player(
            id = "p1",
            name = "Alice",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = mapOf("p2" to Role.VILLAGER),
        )

        db.insertOrReplacePlayer(player)
        val fetched = db.getPlayerById("p1").first()
        assertEquals(player, fetched)
    }

    @Test
    fun testGetPlayer_notExist_returnsNull() = runTest {
        val db = createDb()
        val fetched = db.getPlayerById("missing").firstOrNull()
        assertNull(fetched)
    }

    @Test
    fun testPlayerBoundary_emptyKnownIdentities() = runTest {
        val db = createDb()
        val player = Player(
            id = "p2",
            name = "Bob",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = false,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        db.insertOrReplacePlayer(player)
        val fetched = db.getPlayerById("p2").first()
        assertTrue(fetched?.knownIdentities?.isEmpty() == true)
        assertFalse(fetched.isAlive)
    }

    @Test
    fun testUpdateAliveStatus_changesIsAliveFlag() = runTest {
        val db = createDb()
        val player = Player(
            id = "p1",
            name = "Alice",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        db.insertOrReplacePlayer(player)

        db.updateAliveStatus(false, "p1")
        val fetched = db.getPlayerById("p1").first()
        assertFalse(fetched?.isAlive == true)

        db.updateAliveStatus(true, "p1")
        val fetchedAgain = db.getPlayerById("p1").first()
        assertTrue(fetchedAgain?.isAlive == true)
    }

    @Test
    fun testUpdateLastAction_setsPlayerAction() = runTest {
        val db = createDb()
        val player = Player(
            id = "p1",
            name = "Alice",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        db.insertOrReplacePlayer(player)

        val action = PlayerAction(ActionType.KILL, "target1")
        db.updateLastAction(action, "p1")
        val fetched = db.getPlayerById("p1").first()
        assertEquals(action, fetched?.lastAction)
    }

    @Test
    fun testUpdateKnownIdentities_setsMapCorrectly() = runTest {
        val db = createDb()
        val player = Player(
            id = "p1",
            name = "Bob",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        db.insertOrReplacePlayer(player)

        val known = mapOf("p2" to Role.VILLAGER, "p3" to Role.DETECTIVE)
        db.updateKnownIdentities(known, "p1")
        val fetched = db.getPlayerById("p1").first()
        assertEquals(known, fetched?.knownIdentities)
    }

    @Test
    fun testClearPlayers_removesAllPlayers() = runTest {
        val db = createDb()
        val player1 = Player(
            id = "p1",
            name = "Alice",
            role = Role.VILLAGER,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        val player2 = Player(
            id = "p2",
            name = "Bob",
            role = Role.GONDI,
            avatar = Avatar.Christian,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyMap(),
        )
        db.insertOrReplacePlayer(player1)
        db.insertOrReplacePlayer(player2)

        var allPlayers = db.getAllPlayers().first()
        assertEquals(2, allPlayers.size)

        db.clearPlayers()
        allPlayers = db.getAllPlayers().first()
        assertTrue(allPlayers.isEmpty())
    }

    @Test
    fun testGetAlivePlayers_emitsOnlyAlivePlayers() = runTest {
        // Arrange
        val db = createDb()
        val player1 = Player("1", "Alice", Role.DETECTIVE, null, background = AvatarBackground.GREEN_EMERALD, isAlive = true)
        val player2 = Player("2", "Bob", Role.DETECTIVE, null, background = AvatarBackground.GREEN_EMERALD, isAlive = false)
        val player3 = Player("3", "Charles", Role.DETECTIVE, null, background = AvatarBackground.GREEN_EMERALD, isAlive = true)
        db.insertOrReplacePlayer(player1)
        db.insertOrReplacePlayer(player2)
        db.insertOrReplacePlayer(player3)

        // Act
        val alivePlayers = db.getAlivePlayers().first()

        // Assert
        assertEquals(2, alivePlayers.size)
        assertTrue(alivePlayers.all { it.isAlive })
        assertEquals(setOf("Alice", "Charles"), alivePlayers.map { it.name }.toSet())
    }

    /** -------------------- VOTE -------------------- **/

    @Test
    fun testInsertAndGetVote_success() = runTest {
        val db = createDb()
        val vote = Vote(voterId = "voter1", targetId = "target1", isGuilty = true)
        db.insertOrReplaceVote(vote)

        val fetched = db.getAllVotes().first()
        assertEquals(1, fetched.size)
        assertEquals("voter1", fetched[0].voterId)
        assertEquals("target1", fetched[0].targetId)
        assertTrue(fetched[0].isGuilty)
    }

    @Test
    fun testVoteBoundary_emptyTable() = runTest {
        val db = createDb()
        val votes = db.getAllVotes().first()
        assertTrue(votes.isEmpty())
    }

    @Test
    fun clearVotes_clearsAllVotesFromTable() = runTest {
        val db = createDb()
        val vote = Vote("vote1", "player1", true)

        db.insertOrReplaceVote(vote)
        val beforeClear = db.getAllVotes().first()
        assertEquals(1, beforeClear.size)

        // Act
        db.clearVotes()

        // Assert
        val afterClear = db.getAllVotes().first()
        assertTrue(afterClear.isEmpty())
    }
}
