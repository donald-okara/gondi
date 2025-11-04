package ke.don.remote.server

import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.KnownIdentity
import ke.don.local.db.LocalDatabase
import kotlinx.coroutines.flow.firstOrNull

class DefaultGameEngine(private val db: LocalDatabase) : GameEngine {
    override suspend fun reduce(intent: PlayerIntent) {
        val gameId = db.getFirstGameState().firstOrNull()?.id

        when (intent) {
            is PlayerIntent.Kill -> db.updateAliveStatus(false, intent.targetId)
            is PlayerIntent.Save -> db.updateAliveStatus(true, intent.targetId)
            is PlayerIntent.Accuse -> gameId?.let {
                db.accusePlayer(
                    PlayerAction(
                        ActionType.ACCUSE,
                        intent.targetId
                    ), it)
            }
            is PlayerIntent.Investigate -> {
                val player = db.getPlayerById(id = intent.targetId).firstOrNull()
                player?.knownIdentities?.plus(
                    KnownIdentity(
                        playerId = intent.playerId,
                        role = player.role!!
                    )
                )?.let {
                    db.updateKnownIdentities(
                        it,
                        intent.playerId
                    )
                }
            }
            is PlayerIntent.Second -> gameId?.let {
                db.secondPlayer(
                    PlayerAction(
                        ActionType.SECOND,
                        intent.targetId
                    ), it)
            }
            is PlayerIntent.Vote -> db.insertOrReplaceVote(vote = intent.vote)
        }
    }
}


