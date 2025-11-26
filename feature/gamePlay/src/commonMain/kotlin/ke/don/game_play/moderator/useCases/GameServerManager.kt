/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.useCases

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.utils.result.LocalError
import ke.don.utils.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameServerManager(
    private val server: LocalServer,
) {
    suspend fun startServer(
        gameIdentity: GameIdentity,
        newGame: GameState,
        hostPlayer: Player,
    ): Result<Unit, LocalError> {
        return try {
            server.stop()
            server.start(gameIdentity)
            server.handleModeratorCommand(
                gameIdentity.id,
                ModeratorCommand.CreateGame(gameIdentity.id, newGame, hostPlayer),
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(LocalError(e.message ?: "Unknown error", e.cause.toString()))
        }
    }

    suspend fun stopServer(
    ) {
        runCatching { server.stop() }.onFailure { it.printStackTrace() }
    }
}
