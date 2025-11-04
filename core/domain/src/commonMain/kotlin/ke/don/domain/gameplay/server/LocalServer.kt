package ke.don.domain.gameplay.server

import ke.don.domain.gameplay.ModeratorCommand

interface LocalServer {
    suspend fun start(identity: GameIdentity)
    suspend fun stop()
    suspend fun handleModeratorCommand(command: ModeratorCommand)
}