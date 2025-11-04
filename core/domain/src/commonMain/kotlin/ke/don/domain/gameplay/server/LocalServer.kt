package ke.don.domain.gameplay.server

interface LocalServer {
    suspend fun start(identity: GameIdentity)
    suspend fun stop()
}