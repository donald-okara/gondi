package ke.don.domain.gameplay.server

import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

data class GameIdentity(
    val id: String,
    val serviceHost: String = "",
    val serviceType: String = SERVICE_TYPE,
    val servicePort: Int = 8080,
    val gameName: String,
    val moderatorName: String,
    val moderatorAvatar: Avatar? = null,
    val moderatorAvatarBackground: AvatarBackground
)
