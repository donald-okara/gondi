/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.model

import ke.don.domain.gameplay.ModeratorCommand

sealed interface ModeratorHandler {
    class UpdateAssignments(val assignment: RoleAssignment) : ModeratorHandler
    class UpdateRoomName(val name: String) : ModeratorHandler
    class SelectPlayer(val id: String? = null) : ModeratorHandler
    class HandleModeratorCommand(val intent: ModeratorCommand) : ModeratorHandler
    object StartServer : ModeratorHandler
    object StartGame : ModeratorHandler
    object ShowLeaveDialog : ModeratorHandler
    object ShowRulesModal : ModeratorHandler
}
