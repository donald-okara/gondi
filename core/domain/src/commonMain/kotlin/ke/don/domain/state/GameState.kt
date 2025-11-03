package ke.don.domain.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val id: String,
    val phase: GamePhase,
    val round: Long,
    @SerialName("pending_kills") val pendingKills: List<String> = emptyList(),
    @SerialName("last_saved_player_id") val lastSavedPlayerId: String? = null,
    @SerialName("accused_player_id") val accusedPlayerId: String? = null,
    @SerialName("reveal_eliminated_player") val revealEliminatedPlayer: Boolean,
)
