package ke.don.game_play.moderator.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import ke.don.components.empty_state.EmptyState
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


@Composable
fun LobbyContent(
    modifier: Modifier = Modifier,
    moderatorState: ModeratorState,
    gameState: GameState? = null,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val availableSlots by derivedStateOf { moderatorState.assignment.sumOf { it.second } }
    val playersSize by derivedStateOf{ players.filter { player -> player.isAlive && player.role != Role.MODERATOR}.size }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Ready to Begin?",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                ButtonToken(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = playersSize > 5,
                    buttonType = ComponentType.Primary,
                    onClick = {
                        gameState?.id?.let {
                            onEvent(
                                ModeratorHandler.HandleModeratorCommand(
                                    ModeratorCommand.StartGame(it)
                                )
                            )
                        }
                    }
                ) {
                    AnimatedContent(
                        targetState = availableSlots
                    ) { slots ->
                        Text(
                            text = if (slots > playersSize) "Start with $playersSize players" else "Start Game",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                AnimatedVisibility(visible = availableSlots > playersSize) {
                    Text(
                        text = "Waiting for more players...",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .padding(Theme.spacing.small)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Players in Lobby",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Surface(
                        color = Theme.colorScheme.primary.copy(0.2f),
                        contentColor = Theme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "$playersSize/$availableSlots",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(
                                horizontal = Theme.spacing.small,
                                vertical = Theme.spacing.extraSmall
                            )
                        )
                    }

                }

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize),
                    columns = GridCells.Adaptive(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(players.sortedByDescending { player -> player.isAlive }, key = { it.id }) { player ->
                        PlayerItem(
                            actionType = ActionType.NONE,
                            onClick = {},
                            isSelected = false, // TODO
                            showRole = true,
                            player = player
                        )
                    }

                    val emptySlotCount = availableSlots - playersSize
                    if (emptySlotCount > 0) {
                        items(emptySlotCount) {
                            EmptySlot()
                        }
                    }
                }
            }
        }
    }
}
