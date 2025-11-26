package ke.don.game_play.player.model

import ke.don.utils.result.ReadStatus

data class PlayerState(
    val lastPing: Long = 0,
    val connectionStatus: ReadStatus = ReadStatus.Loading,
    val showLeaveGame: Boolean = false,
)
