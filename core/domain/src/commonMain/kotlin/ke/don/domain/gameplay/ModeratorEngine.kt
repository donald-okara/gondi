package ke.don.domain.gameplay

interface ModeratorEngine {
    suspend fun handle(command: ModeratorCommand)
}

interface GameEngine {
    suspend fun reduce(intent: PlayerIntent)
}
