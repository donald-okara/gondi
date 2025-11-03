package ke.don.domain.state

import kotlinx.serialization.Serializable

@Serializable
enum class GamePhase {
    LOBBY,
    SLEEP,
    TOWN_HALL,
    COURT,
    GAME_OVER
}
