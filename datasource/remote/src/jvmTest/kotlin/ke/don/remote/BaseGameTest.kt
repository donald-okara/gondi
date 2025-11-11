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

import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.local.db.DatabaseFactory
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
        name = "game1",
        phase = GamePhase.LOBBY,
        round = 0L,
        winners = null,
        pendingKills = emptyList(),
        lastSavedPlayerId = null,
        accusedPlayer = null,
        second = null,
        revealEliminatedPlayer = false,
    )

    val player1 = Player(
        id = "player1",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Christian,
        name = "Otis",
        timeOfDeath = null,
        background = AvatarBackground.GREEN_EMERALD,
    )
    val player2 = Player(
        id = "player2",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Leo,
        name = "Noah",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_ORCHID,
    )
    val player3 = Player(
        id = "player3",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Adrian,
        name = "Kellen",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_ORCHID,
    )
    val player4 = Player(
        id = "player4",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Amaya,
        name = "Elliot",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_ORCHID,
    )
    val player5 = Player(
        id = "player5",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Ryker,
        name = "Julian",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_LILAC,
    )
    val player6 = Player(
        id = "player6",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.George,
        name = "Theodore",
        timeOfDeath = null,
        background = AvatarBackground.YELLOW_SUNNY,
    )
    val player7 = Player(
        id = "player7",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Nolan,
        name = "Levi",
        timeOfDeath = null,
        background = AvatarBackground.PINK_HOT,
    )
    val player8 = Player(
        id = "player8",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Sawyer,
        name = "Arthur",
        timeOfDeath = null,
        background = AvatarBackground.YELLOW_SUNNY,
    )
    val player9 = Player(
        id = "player9",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.Aidan,
        name = "Jasper",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_LILAC,
    )
    val player10 = Player(
        id = "player10",
        role = null,
        isAlive = true,
        lastAction = null,
        knownIdentities = emptyList(),
        avatar = Avatar.George,
        name = "Asher",
        timeOfDeath = null,
        background = AvatarBackground.PURPLE_ORCHID,
    )

    val batchUpdateRoles = listOf(
        player1.copy(role = Role.VILLAGER),
        player2.copy(role = Role.VILLAGER),
        player3.copy(role = Role.VILLAGER),
        player4.copy(role = Role.VILLAGER),
        player5.copy(role = Role.VILLAGER),
        player6.copy(role = Role.DOCTOR),
        player7.copy(role = Role.ACCOMPLICE),
        player8.copy(role = Role.DETECTIVE),
        player9.copy(role = Role.GONDI),
        player10.copy(role = Role.GONDI),
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
        db.transaction {
            db.insertOrReplacePlayer(player1)
            db.insertOrReplacePlayer(player2)
            db.insertOrReplacePlayer(player3)
            db.insertOrReplacePlayer(player4)
            db.insertOrReplacePlayer(player5)
            db.insertOrReplacePlayer(player6)
            db.insertOrReplacePlayer(player7)
            db.insertOrReplacePlayer(player8)
            db.insertOrReplacePlayer(player9)
            db.insertOrReplacePlayer(player10)
            db.insertOrReplaceGameState(gameState)
        }
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
    ) {
        val logger = Logger("Validation")
        if (validateIntent(db, gameState.id, intent)) {
            engine.reduce(gameState.id, intent)
        } else {
            val error = "Invalid intent for player ${intent.playerId}"
            logger.error(error)
        }
    }
}
