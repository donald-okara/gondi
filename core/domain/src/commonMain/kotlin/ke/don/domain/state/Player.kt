package ke.don.domain.state

import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role

data class Player(
    val id: String,
    val name: String,
    val role: Role,
    val isAlive: Boolean = true,
    val lastAction: PlayerAction? = null,
    val knownIdentities: Map<String, Role?> = emptyMap()
)
