package ke.don.domain.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    @SerialName("voter_id") val voterId: String,
    @SerialName("target_id") val targetId: String,
    @SerialName("is_guilty") val isGuilty: Boolean
)
