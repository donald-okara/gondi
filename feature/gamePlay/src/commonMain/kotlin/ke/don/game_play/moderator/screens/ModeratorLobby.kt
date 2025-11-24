package ke.don.game_play.moderator.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.components.EmptySlot
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.moderator.useCases.PLAYER_LOWER_LIMIT
import ke.don.game_play.shared.SharedLobby


@Composable
fun ModeratorLobby(
    modifier: Modifier = Modifier,
    gameState: GameState? = null,
    hostPlayer: Player?,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    SharedLobby(
        modifier = modifier,
        isModerator = true,
        gameState = gameState,
        myPlayerId = hostPlayer?.id,
        players = players,
        startGame = {
            gameState?.id?.let {
                onEvent(
                    ModeratorHandler.HandleModeratorCommand(
                        ModeratorCommand.StartGame(it)
                    )
                )
            }
        }
    )
}
