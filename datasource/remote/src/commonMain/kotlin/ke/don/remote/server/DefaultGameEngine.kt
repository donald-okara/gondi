/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.server

import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.KnownIdentity
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

class DefaultGameEngine(private val db: LocalDatabase) : GameEngine {
    override suspend fun reduce(gameId: String, intent: PlayerIntent) {
        when (intent) {
            is PlayerIntent.Join -> db.insertOrReplacePlayer(intent.player)
            is PlayerIntent.Kill -> db.killAction(
                PlayerAction(
                    type = ActionType.KILL,
                    playerId = intent.playerId,
                    targetId = intent.targetId,
                ),
            )

            is PlayerIntent.Save -> db.saveAction(
                PlayerAction(
                    type = ActionType.SAVE,
                    playerId = intent.playerId,
                    targetId = intent.targetId,
                ),
            )
            is PlayerIntent.Accuse -> db.accusePlayer(
                PlayerAction(
                    type = ActionType.ACCUSE,
                    playerId = intent.playerId,
                    targetId = intent.targetId,
                ),
                gameId,
            )
            is PlayerIntent.Investigate -> {
                val player = db.getPlayerById(id = intent.targetId).firstOrNull()
                player?.knownIdentities?.plus(
                    KnownIdentity(
                        playerId = intent.playerId,
                        role = player.role!!,
                    ),
                )?.let {
                    db.updateKnownIdentities(
                        knownIdentities = it,
                        id = intent.playerId,
                    )
                }
            }
            is PlayerIntent.Second -> gameId?.let {
                db.secondPlayer(
                    PlayerAction(
                        type = ActionType.SECOND,
                        playerId = intent.playerId,
                        targetId = intent.targetId,
                    ),
                    it,
                )
            }
            is PlayerIntent.Vote -> db.insertOrReplaceVote(vote = intent.vote)
        }
    }
}
