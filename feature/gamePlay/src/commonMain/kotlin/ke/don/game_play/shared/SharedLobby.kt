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

package ke.don.game_play.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.empty_state.EmptyState
import ke.don.components.icon.IconToken
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Role
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.components.EmptySlot
import ke.don.game_play.moderator.model.Announcement
import ke.don.game_play.moderator.useCases.PLAYER_LOWER_LIMIT
import ke.don.utils.toFormattedTime
import kotlin.time.ExperimentalTime

@Composable
fun SharedLobby(
    modifier: Modifier = Modifier,
    isModerator: Boolean,
    gameState: GameState? = null,
    onSelectPlayer: (String) -> Unit = {},
    players: List<Player>,
    myPlayerId: String? = null,
    startGame: () -> Unit = {},
    announcements: List<Announcement> = emptyList(),
    onShowRules: () -> Unit = {},
) {
    val nonModeratorPlayers = players.filter { it.role != Role.MODERATOR }
    val alivePlayers = nonModeratorPlayers
        .filter { it.isAlive }
        .sortedWith(compareByDescending { it.role != null })

    val playersSize = alivePlayers.size
    val moderator = players.find { it.role == Role.MODERATOR }
    val availableSlots = gameState?.availableSlots ?: 0

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            LobbyHeader(
                modifier = Modifier,
                playersSize,
                availableSlots.toInt(),
                startGame,
                isModerator
            )
        }
        item { RoleLockWarning(modifier = Modifier, nonModeratorPlayers, isModerator) }
        item {
            ModeratorSection(
                moderator,
                myPlayerId,
                announcements = announcements,
                onShowRules = onShowRules
            )
        }
        item { PlayersGrid(alivePlayers, availableSlots.toInt(), myPlayerId, onSelectPlayer) }
    }
}

@Composable
fun LobbyHeader(
    modifier: Modifier = Modifier,
    playersSize: Int,
    availableSlots: Int,
    startGame: () -> Unit,
    isModerator: Boolean,
) {
    if (!isModerator) return

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
    ) {
        Text(
            text = "Ready to Begin?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start),
        )

        ButtonToken(
            modifier = Modifier.fillMaxWidth(),
            enabled = playersSize > PLAYER_LOWER_LIMIT,
            buttonType = ComponentType.Primary,
            onClick = startGame,
        ) {
            AnimatedContent(targetState = availableSlots) { slots ->
                Text(
                    text = if (slots > playersSize) "Start with $playersSize players" else "Start Game",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        AnimatedVisibility(visible = availableSlots > playersSize) {
            Text(
                text = "Waiting for more players...",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RoleLockWarning(
    modifier: Modifier = Modifier,
    nonModeratorPlayers: List<Player>,
    isModerator: Boolean,
) {
    if (!isModerator) return

    AnimatedVisibility(nonModeratorPlayers.any { it.role != null }) {
        Text(
            text = "At least one player has a role. This locks everyone else out from joining the game",
            color = Theme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ModeratorSection(
    moderator: Player?,
    myPlayerId: String?,
    modifier: Modifier = Modifier,
    announcements: List<Announcement> = emptyList(),
    onShowRules: () -> Unit = {},
) {
    if (moderator == null) return

    Surface(
        shape = Theme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium)
        ) {
            ModeratorPanelHeader(onClick = onShowRules)

            // Moderator info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
            ) {
                PlayerItem(
                    actionType = ActionType.NONE,
                    isSelected = false,
                    isMe = myPlayerId == moderator.id,
                    showRole = true,
                    enabled = false,
                    player = moderator,
                )
                AnnouncementSection(announcements = announcements)
            }
        }
    }
}

@Composable
private fun ModeratorPanelHeader(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            "Moderator Panel",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        IconToken(
            imageVector = Icons.Outlined.AutoStories,
            buttonType = ComponentType.Inverse,
            onClick = onClick,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnnouncementSection(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Announcements",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        AnimatedContent(
            targetState = announcements,
            modifier = Modifier.fillMaxWidth(),
        ) { items ->
            if (items.isEmpty()) {
                EmptyState(
                    title = "Nothing to see yet",
                    description = "Announcements will show up here.",
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
                        modifier = Modifier.matchParentSize()
                    ) {
                        items(items.size, key = { it }) { index ->
                            val announcement = items[index]
                            AnnouncementBubble(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                announcement = announcement
                            )
                        }
                    }
                    // Fade overlay for scrollable content
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                    0.1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                                    0.3f to Color.Transparent,
                                    0.7f to Color.Transparent,
                                    0.9f to MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                                    1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                                )
                            )
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun AnnouncementBubble(
    modifier: Modifier = Modifier,
    announcement: Announcement,
) {
    Surface(
        shape = Theme.shapes.medium,
        color = Theme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        contentColor = Theme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ){
            Text(
                text = announcement.first,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(Theme.spacing.medium)
            )
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = Theme.spacing.medium,
                        vertical = Theme.spacing.small
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = announcement.second.toFormattedTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}


@Composable
fun PlayersGrid(
    alivePlayers: List<Player>,
    availableSlots: Int,
    myPlayerId: String?,
    onSelectPlayer: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val playersSize = alivePlayers.size

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(Theme.spacing.small)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Players in Lobby",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = Theme.colorScheme.primary.copy(0.2f),
                contentColor = Theme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    text = "$playersSize/$availableSlots",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        horizontal = Theme.spacing.small,
                        vertical = Theme.spacing.extraSmall,
                    ),
                )
            }
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize),
            columns = GridCells.Adaptive(130.dp),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
            contentPadding = PaddingValues(vertical = Theme.spacing.small),
        ) {
            gridItems(alivePlayers, key = { it.id }) { player ->
                PlayerItem(
                    actionType = ActionType.NONE,
                    onClick = { onSelectPlayer(player.id) },
                    isSelected = false,
                    showRole = player.id == myPlayerId || player.role != null,
                    isMe = myPlayerId == player.id,
                    player = player,
                )
            }

            val emptySlotCount = availableSlots - playersSize
            if (emptySlotCount > 0) {
                items(emptySlotCount) { EmptySlot() }
            }
        }
    }
}
