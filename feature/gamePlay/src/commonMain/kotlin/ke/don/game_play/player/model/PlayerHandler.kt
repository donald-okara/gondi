package ke.don.game_play.player.model

import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ServerId

sealed interface PlayerHandler {
    class Connect(val serverId: ServerId): PlayerHandler
    class Send(val message: PlayerIntent): PlayerHandler
    object ShowLeaveDialog : PlayerHandler
}