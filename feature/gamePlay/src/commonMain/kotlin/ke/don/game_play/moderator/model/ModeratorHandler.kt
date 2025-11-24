package ke.don.game_play.moderator.model

import ke.don.domain.gameplay.ModeratorCommand

sealed interface ModeratorHandler {
    class UpdateAssignments(val assignment: RoleAssignment): ModeratorHandler
    class UpdateRoomName(val name: String): ModeratorHandler
    class HandleModeratorCommand(val intent: ModeratorCommand): ModeratorHandler
    object StartServer: ModeratorHandler
    object StartGame: ModeratorHandler
    object ShowLeaveDialog: ModeratorHandler
}
