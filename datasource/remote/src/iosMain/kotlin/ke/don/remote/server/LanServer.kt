package ke.don.remote.server

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LocalServer

class LanServer(
    private val port: Int
) : LocalServer {
    override suspend fun start(identity: GameIdentity) {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    override suspend fun handleModeratorCommand(command: ModeratorCommand) {
        TODO("Not yet implemented")
    }
}

