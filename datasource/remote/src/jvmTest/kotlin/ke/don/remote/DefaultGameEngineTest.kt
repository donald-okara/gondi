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

import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.remote.server.DefaultGameEngine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class DefaultGameEngineTest : BaseGameTest() {
    /**
     * JOIN
     */

    @Test
    fun testJoinGame_success() = runTest {
        val engine = DefaultGameEngine(db)

        val newPlayer = Player(
            id = "playerTest",
            name = "Johnny Test",
            role = null,
            avatar = Avatar.Katherine,
            timeOfDeath = null,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyList(),
        )

        executeValidated(engine, PlayerIntent.Join(newPlayer.id, newPlayer))

        val fetched = db.getPlayerById(newPlayer.id).first()
        assertEquals(newPlayer, fetched)
    }

    @Test
    fun testJoinGame_error() = runTest {
        val engine = DefaultGameEngine(db)

        val newPlayer = Player(
            id = "playerTest",
            name = "Johnny Test",
            role = null,
            avatar = Avatar.Katherine,
            timeOfDeath = null,
            background = AvatarBackground.GREEN_EMERALD,
            isAlive = true,
            lastAction = null,
            knownIdentities = emptyList(),
        )

        db.batchUpdatePlayerRole(batchUpdateRoles)

        executeValidated(engine, PlayerIntent.Join(newPlayer.id, newPlayer))

        val fetched = db.getPlayerById(newPlayer.id).first()
        assertNull(fetched)
    }

    /**
     * KILL
     */
    @Test
    fun testKillPlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val gondi = player9
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }
        executeValidated(engine, PlayerIntent.Kill(gondi.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.pendingKills
        assert(fetched?.contains(target.id) == true)
    }

    @Test
    fun testKillPlayer_errorWhenNotSleep() = runTest {
        val engine = DefaultGameEngine(db)

        val gondi = player9
        val target = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)

        executeValidated(engine, PlayerIntent.Kill(gondi.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.pendingKills
        assert(fetched?.contains(target.id) == false)
    }

    @Test
    fun testKillPlayer_errorWhenNotGondi() = runTest {
        val engine = DefaultGameEngine(db)

        val gondi = player2
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }
        executeValidated(engine, PlayerIntent.Kill(gondi.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.pendingKills
        assert(fetched?.contains(target.id) == false)
    }

    @Test
    fun testKillPlayer_errorWhenSuicide() = runTest {
        val engine = DefaultGameEngine(db)

        val gondi = player9
        val target = player9

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }
        executeValidated(engine, PlayerIntent.Kill(gondi.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.pendingKills
        assert(fetched?.contains(target.id) == false)
    }

    @Test
    fun testKillPlayer_errorWhenDead() = runTest {
        val engine = DefaultGameEngine(db)

        val gondi = player9
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updateAliveStatus(false, gondi.id)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }
        executeValidated(engine, PlayerIntent.Kill(gondi.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.pendingKills
        assert(fetched?.contains(target.id) == false)
    }

    /**
     * SAVE
     */
    @Test
    fun testSavePlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val doctor = player6
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }

        executeValidated(engine, PlayerIntent.Save(doctor.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.lastSavedPlayerId
        assertEquals(target.id, fetched)
    }

    @Test
    fun testSavePlayer_errorWhenNotDoctor() = runTest {
        val engine = DefaultGameEngine(db)

        val doctor = player3
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }

        executeValidated(engine, PlayerIntent.Save(doctor.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.lastSavedPlayerId
        assertNotEquals(target.id, fetched)
    }

    @Test
    fun testSavePlayer_errorWhenNotAsleep() = runTest {
        val engine = DefaultGameEngine(db)

        val doctor = player6
        val target = player1

        db.batchUpdatePlayerRole(batchUpdateRoles)

        executeValidated(engine, PlayerIntent.Save(doctor.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.lastSavedPlayerId
        assertNotEquals(target.id, fetched)
    }

    @Test
    fun testSavePlayer_errorWhenDead() = runTest {
        val engine = DefaultGameEngine(db)

        val doctor = player6
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updateAliveStatus(isAlive = false, doctor.id)
        }

        executeValidated(engine, PlayerIntent.Save(doctor.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.lastSavedPlayerId
        assertNotEquals(target.id, fetched)
    }

    /**
     * INVESTIGATE
     */
    @Test
    fun testInvestigatePlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val detective = player8
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }

        executeValidated(engine, PlayerIntent.Investigate(detective.id, target.id))
        val fetched = db.getPlayerById(detective.id)
        val updatedTarget = db.getPlayerById(target.id).first()?.toKnownIdentity()

        assert(fetched.first()?.knownIdentities?.contains(updatedTarget) == true)
    }

    @Test
    fun testInvestigatePlayer_errorWhenNotDetective() = runTest {
        val engine = DefaultGameEngine(db)

        val detective = player3
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
        }

        executeValidated(engine, PlayerIntent.Investigate(detective.id, target.id))
        val fetched = db.getPlayerById(detective.id)
        val updatedTarget = db.getPlayerById(target.id).first()?.toKnownIdentity()

        assert(fetched.first()?.knownIdentities?.contains(updatedTarget) == false)
    }

    @Test
    fun testInvestigatePlayer_errorWhenNotAsleep() = runTest {
        val engine = DefaultGameEngine(db)

        val detective = player8
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, 1L, gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertNotEquals(GamePhase.SLEEP, phase)

        executeValidated(engine, PlayerIntent.Investigate(detective.id, target.id))
        val fetched = db.getPlayerById(detective.id)
        val updatedTarget = db.getPlayerById(target.id).first()?.toKnownIdentity()

        assert(fetched.first()?.knownIdentities?.contains(updatedTarget) == false)
    }

    @Test
    fun testInvestigatePlayer_errorWhenDead() = runTest {
        val engine = DefaultGameEngine(db)

        val detective = player8
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, 1L, gameState.id)
            db.updateAliveStatus(false, detective.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.SLEEP, phase)

        executeValidated(engine, PlayerIntent.Investigate(detective.id, target.id))
        val fetched = db.getPlayerById(detective.id)
        val updatedTarget = db.getPlayerById(target.id).first()?.toKnownIdentity()

        assert(fetched.first()?.knownIdentities?.contains(updatedTarget) == false)
    }

    @Test
    fun testAccusePlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player3
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, 1L, gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.accusedPlayer

        assertEquals(target.id, fetched?.targetId)
        assertEquals(accuser.id, fetched?.playerId)
    }

    @Test
    fun testAccusePlayer_errorWhenDead() = runTest {
        val engine = DefaultGameEngine(db)
        val accuser = player3
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
            db.updateAliveStatus(false, accuser.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.accusedPlayer

        assertNull(fetched)
    }

    @Test
    fun testAccusePlayer_errorWhenNotInTownHall() = runTest {
        val engine = DefaultGameEngine(db)
        val accuser = player3
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, round = 1L, id = gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.SLEEP, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.accusedPlayer

        assertNull(fetched)
    }

    @Test
    fun testAccusePlayer_errorWhenSelfAccusing() = runTest {
        val engine = DefaultGameEngine(db)
        val accuser = player3
        val target = player3

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.accusedPlayer

        assertNull(fetched)
    }

    /**
     * SECOND
     */

    @Test
    fun testSecondPlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player3
        val seconder = player2
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }
        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        executeValidated(engine, PlayerIntent.Second(seconder.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.second

        assertEquals(target.id, fetched?.targetId)
        assertEquals(seconder.id, fetched?.playerId)
    }

    @Test
    fun testSecondPlayer_errorWhenDead() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player3
        val seconder = player2
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
            db.updateAliveStatus(false, seconder.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        executeValidated(engine, PlayerIntent.Second(seconder.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.second

        assertNotEquals(target.id, fetched?.targetId)
        assertNotEquals(seconder.id, fetched?.playerId)
    }

    @Test
    fun testSecondPlayer_errorWhenNotTownHall() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player3
        val seconder = player2
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.SLEEP, round = 1L, id = gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.SLEEP, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        executeValidated(engine, PlayerIntent.Second(seconder.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.second

        assertNotEquals(target.id, fetched?.targetId)
        assertNotEquals(seconder.id, fetched?.playerId)
    }

    @Test
    fun testSecondPlayer_errorWhenSelfSecond() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player3
        val seconder = player2
        val target = player2

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        executeValidated(engine, PlayerIntent.Second(seconder.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.second

        assertNull(fetched?.playerId)
    }

    @Test
    fun testSecondPlayer_errorWhenSelfAccusing() = runTest {
        val engine = DefaultGameEngine(db)

        val accuser = player2
        val seconder = player2
        val target = player1

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, target.id))

        executeValidated(engine, PlayerIntent.Second(seconder.id, target.id))

        val fetched = db.getGameState(gameState.id).first()?.second

        assertNull(fetched?.playerId)
    }

    /**
     * Vote
     */
    @Test
    fun testVotePlayer_success() = runTest {
        val engine = DefaultGameEngine(db)

        val voter = player1
        val accused = player2
        val accuser = player3

        val vote = Vote(
            voterId = voter.id,
            targetId = accused.id,
            isGuilty = false,
        )

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, accused.id))

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        db.updatePhase(GamePhase.COURT, 1L, gameState.id)
        executeValidated(engine, PlayerIntent.Vote(voter.id, vote))

        val fetched = db.getAllVotes().first()

        assert(fetched.contains(vote))
    }

    @Test
    fun testVotePlayer_errorWhenVoteForSelf() = runTest {
        val engine = DefaultGameEngine(db)

        val voter = player1
        val accused = player1
        val accuser = player2

        val vote = Vote(
            voterId = voter.id,
            targetId = accused.id,
            isGuilty = false,
        )

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, accused.id))

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        db.updatePhase(GamePhase.COURT, 1L, gameState.id)
        executeValidated(engine, PlayerIntent.Vote(voter.id, vote))

        val fetched = db.getAllVotes().first()

        assert(!fetched.contains(vote))
    }

    @Test
    fun testVotePlayer_errorWhenVotesTwice() = runTest {
        val engine = DefaultGameEngine(db)

        val voter = player1
        val accused = player2
        val accuser = player3

        val vote = Vote(
            voterId = voter.id,
            targetId = accused.id,
            isGuilty = false,
        )

        db.transaction {
            db.batchUpdatePlayerRole(batchUpdateRoles)
            db.updatePhase(GamePhase.TOWN_HALL, round = 1L, id = gameState.id)
        }

        executeValidated(engine, PlayerIntent.Accuse(accuser.id, accused.id))

        val phase = db.getGameState(gameState.id).first()?.phase
        assertEquals(GamePhase.TOWN_HALL, phase)

        db.updatePhase(GamePhase.COURT, 1L, gameState.id)
        executeValidated(engine, PlayerIntent.Vote(voter.id, vote))
        executeValidated(engine, PlayerIntent.Vote(voter.id, vote))

        val fetched = db.getAllVotes().first()

        assert(fetched.size == 1)
    }
}
