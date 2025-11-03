package ke.don.domain.state

import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val role: Role? = null,
    val avatar: Avatar? = null,
    val background: AvatarBackground = AvatarBackground.entries.first(),
    @SerialName("is_alive") val isAlive: Boolean = true,
    @SerialName("last_action") val lastAction: PlayerAction? = null,
    @SerialName("known_identities") val knownIdentities: Map<String, Role?> = emptyMap()
)
