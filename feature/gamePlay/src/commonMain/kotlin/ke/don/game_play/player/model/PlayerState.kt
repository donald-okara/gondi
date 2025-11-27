@file:OptIn(ExperimentalTime::class)

package ke.don.game_play.player.model

import ke.don.game_play.moderator.model.Announcement
import ke.don.utils.result.ReadStatus
import kotlin.time.ExperimentalTime

data class PlayerState(
    val lastPing: Long = 0,
    val connectionStatus: ReadStatus = ReadStatus.Loading,
    val showLeaveGame: Boolean = false,
    val showRulesModal: Boolean = false,
    val announcements: List<Announcement> = emptyList(),
)
