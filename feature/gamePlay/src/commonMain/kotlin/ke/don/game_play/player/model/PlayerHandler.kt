/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.model

import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.server.ServerId

sealed interface PlayerHandler {
    class Connect(val serverId: ServerId) : PlayerHandler
    class Send(val message: PlayerIntent) : PlayerHandler
    class SelectPlayer(val playerId: String?) : PlayerHandler
    object ShowLeaveDialog : PlayerHandler
    object RevealDeaths : PlayerHandler
    object ShowVoteDialog : PlayerHandler
    object ShowRulesModal : PlayerHandler
}
