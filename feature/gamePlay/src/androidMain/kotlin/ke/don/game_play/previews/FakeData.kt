/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.previews

import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.domain.state.KnownIdentity
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object FakeData {
    val players = listOf(
        Player(
            id = "1",
            name = "Matt Foley",
            role = Role.VILLAGER,
            Avatar.Alexander,
            background = AvatarBackground.PURPLE_LILAC,
        ),
        Player(
            id = "2",
            name = "Stefon Zelesky",
            role = Role.VILLAGER,
            Avatar.Christian,
            background = AvatarBackground.PINK_HOT,
        ),
        Player(
            id = "3",
            name = "David S. Pumpkins",
            role = Role.GONDI,
            Avatar.Amaya,
            background = AvatarBackground.YELLOW_BANANA,
        ),
        Player(
            id = "4",
            name = "Roseanne Roseannadanna",
            role = Role.DETECTIVE,
            Avatar.Aidan,
            background = AvatarBackground.GREEN_LEAFY,
        ),
        Player(
            id = "5",
            name = "Todd O'Connor",
            role = Role.VILLAGER,
            Avatar.Kimberly,
            background = AvatarBackground.ORANGE_CORAL,
        ),
        Player(
            id = "6",
            name = "Pat O'Neill",
            role = Role.VILLAGER,
            Avatar.George,
            background = AvatarBackground.PURPLE_AMETHYST,
        ),
        Player(
            id = "7",
            name = "Hans",
            role = Role.VILLAGER,
            Avatar.Jocelyn,
            background = AvatarBackground.GREEN_MINTY,
        ),
        Player(
            id = "8",
            name = "Franz",
            role = Role.MODERATOR,
            Avatar.Jameson,
            background = AvatarBackground.YELLOW_GOLDEN,
        ),
    )

    val gameState = GameState(
        pendingKills = listOf("1", "2"),
        lastSavedPlayerId = "3",
        round = 1,
    )

    val announcements: List<Pair<String, Instant>> = List(10) {
        "Matt Foley just joined" to Clock.System.now()
    }

    val votes = players.filter { it.id != "2" }.map {
        Vote(
            voterId = it.id,
            targetId = "2",
            isGuilty = it.id.toInt() % 2 == 0,
        )
    }

    fun currentPlayer(role: Role) = Player(
        id = "18",
        name = "Franz",
        role = role,
        Avatar.Jameson,
        background = AvatarBackground.YELLOW_GOLDEN,
        knownIdentities = listOf(
            KnownIdentity(
                playerId = "1",
                role = Role.MODERATOR,
                round = 1,
            ),
        ),
    )

    val accuser = players[0]
    val accused = players[1]
    val seconder = players[4]

    val townHallGameState = GameState(
        accusedPlayer = PlayerAction(
            round = 1,
            type = ActionType.ACCUSE,
            targetId = accused.id,
            playerId = accuser.id,
        ),
        second = PlayerAction(
            round = 1,
            type = ActionType.SECOND,
            targetId = accused.id,
            playerId = seconder.id,
        ),
    )
}
