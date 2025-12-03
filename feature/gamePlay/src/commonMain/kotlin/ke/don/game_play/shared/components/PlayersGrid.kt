/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.shared.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.empty_state.EmptyState
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.SelectedPlayer
import ke.don.domain.state.Player
import ke.don.game_play.moderator.components.EmptySlot
import ke.don.game_play.moderator.model.Announcement
import ke.don.utils.toFormattedTime
import kotlin.time.ExperimentalTime
import androidx.compose.foundation.lazy.grid.items as gridItems

@Composable
fun PlayersGrid(
    modifier: Modifier = Modifier,
    myPlayerId: String?,
    onSelectPlayer: (String) -> Unit,
    availableSlots: Int,
    showEmpty: Boolean = true,
    alivePlayers: List<Player>,
    isModerator: Boolean = false,
    knownIdentities: List<String> = emptyList(),
    selectedPlayers: List<SelectedPlayer> = emptyList(),
) {
    val sortedPlayers = remember(alivePlayers, myPlayerId) {
        alivePlayers.sortedWith(
            compareByDescending<Player> { it.id == myPlayerId } // Rule 1: "Me" first
                .thenByDescending { it.role != null } // Rule 2: Players with roles next
                .thenByDescending { knownIdentities.contains(it.id) }, // Rule 2: Players with roles next
        )
    }

    val playersSize = alivePlayers.size

    val selectionMap = remember(selectedPlayers) {
        selectedPlayers.associateBy({ it.first }, { it.second })
    }

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize),
        columns = GridCells.Adaptive(130.dp),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        contentPadding = PaddingValues(vertical = Theme.spacing.small),
    ) {
        gridItems(sortedPlayers, key = { it.id }) { player ->

            val selectionAction = selectionMap[player.id]

            PlayerItem(
                actionType = selectionAction ?: ActionType.NONE,
                onClick = { onSelectPlayer(player.id) },
                isSelected = selectionAction != null,
                showRole = player.id == myPlayerId || knownIdentities.contains(player.id),
                isMe = myPlayerId == player.id,
                player = player,
                enabled = true,
            )
        }

        val emptySlotCount = availableSlots - playersSize
        if (emptySlotCount > 0 && showEmpty) {
            items(emptySlotCount) { EmptySlot() }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalTime::class)
@Composable
fun AnnouncementSection(
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
                        .height(220.dp),
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
                        modifier = Modifier.matchParentSize(),
                    ) {
                        items(items.size, key = { items[it].hashCode() }) { index ->
                            val announcement = items[index]
                            AnnouncementBubble(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                announcement = announcement,
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
                                    1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                ),
                            ),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun AnnouncementBubble(
    modifier: Modifier = Modifier,
    announcement: Announcement,
) {
    Surface(
        shape = Theme.shapes.medium,
        color = Theme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        contentColor = Theme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = announcement.first,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(Theme.spacing.medium),
            )
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = Theme.spacing.medium,
                        vertical = Theme.spacing.small,
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
