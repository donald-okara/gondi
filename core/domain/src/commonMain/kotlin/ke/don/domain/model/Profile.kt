package ke.don.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name : String = "",
    val avatar: Avatar? = null,
    val email: String = "",
    val background: AvatarBackground = AvatarBackground.entries.random()
)
