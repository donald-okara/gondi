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

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.ServerUpdate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class LanServer(
    private val port: Int,
) : LocalServer {
    private val _localEvents = MutableSharedFlow<ServerUpdate.Announcement>()
    override val localEvents: SharedFlow<ServerUpdate.Announcement> = _localEvents

    override suspend fun start(identity: GameIdentity) {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    override suspend fun handleModeratorCommand(
        gameId: String,
        command: ModeratorCommand,
    ) {
        TODO("Not yet implemented")
    }
}
