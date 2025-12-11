/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
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
    val revealDeaths: Boolean = false,
    val selectedId: String? = null,
    val showVote: Boolean = false,
    val announcements: List<Announcement> = emptyList(),
)
