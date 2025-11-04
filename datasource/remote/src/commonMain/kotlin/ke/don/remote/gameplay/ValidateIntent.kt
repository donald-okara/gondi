package ke.don.remote.gameplay

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GamePhase
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

suspend fun validateIntent(
    db: LocalDatabase,
    intent: PlayerIntent,
    currentPhase: GamePhase
): Boolean {
    val player = db.getPlayerById(intent.playerId).firstOrNull() ?: return false

    return when (intent) {
        is PlayerIntent.Kill ->
            player.isAlive &&
                    currentPhase == GamePhase.SLEEP &&
                    player.role?.faction == Faction.GONDI &&
                    player.role?.canActInSleep == true

        is PlayerIntent.Save ->
            player.isAlive &&
                    currentPhase == GamePhase.SLEEP &&
                    player.role == Role.DOCTOR &&
                    player.role?.canActInSleep == true

        is PlayerIntent.Investigate ->
            player.isAlive &&
                    currentPhase == GamePhase.SLEEP &&
                    player.role == Role.DETECTIVE &&
                    player.role?.canActInSleep == true

        is PlayerIntent.Accuse, is PlayerIntent.Second ->
            player.isAlive &&
                    player.role?.canAccuse == true &&
                    player.role?.canVote == true &&
                    currentPhase == GamePhase.TOWN_HALL

        is PlayerIntent.Vote ->
            player.isAlive &&
                    player.role?.canVote == true &&
                    currentPhase == GamePhase.COURT
    }
}
