package ke.don.domain.gameplay

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class PlayerAction @OptIn(ExperimentalTime::class) constructor(
    val type: ActionType,
    val targetId: String? = null,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

enum class ActionType {
    KILL,
    SAVE,
    INVESTIGATE,
    ACCUSE,
    SECOND,
    VOTE_GUILTY,
    VOTE_INNOCENT,
    NONE
}
