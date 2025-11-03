package ke.don.domain.gameplay

import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player

sealed class PlayerIntent {
    data class Kill(val targetId: String) : PlayerIntent()
    data class Save(val targetId: String) : PlayerIntent()
    data class Investigate(val targetId: String) : PlayerIntent()
    data class Accuse(val targetId: String) : PlayerIntent()
    data class Second(val targetId: String) : PlayerIntent()
    data class Vote(val guilty: Boolean) : PlayerIntent()
}

fun validateIntent(player: Player, intent: PlayerIntent, currentPhase: GamePhase): Boolean {
    return when (intent) {
        is PlayerIntent.Kill -> player.role == Role.GONDI && currentPhase == GamePhase.SLEEP && player.isAlive
        is PlayerIntent.Save -> player.role == Role.DOCTOR && currentPhase == GamePhase.SLEEP && player.isAlive
        is PlayerIntent.Investigate -> player.role == Role.DETECTIVE && currentPhase == GamePhase.SLEEP && player.isAlive
        is PlayerIntent.Accuse -> player.isAlive && currentPhase == GamePhase.TOWN_HALL
        is PlayerIntent.Second -> player.isAlive && currentPhase == GamePhase.TOWN_HALL
        is PlayerIntent.Vote -> player.isAlive && currentPhase == GamePhase.COURT
    }
}
