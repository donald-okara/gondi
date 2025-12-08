/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.state.Player
import ke.don.game_play.moderator.model.Announcement
import ke.don.game_play.shared.components.AccusationSection
import ke.don.game_play.shared.components.AnnouncementSection
import ke.don.game_play.shared.components.PlayersGrid
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SharedTownHall(
    players: List<Player>,
    onSelectPlayer: (String) -> Unit,
    onVote: () -> Unit = {},
    myPlayerId: String,
    seconder: Player?,
    accuser: Player?,
    accused: Player?,
    knownIdentity: List<String> = emptyList(),
    actingPlayers: List<String> = emptyList(),
    onSecond: () -> Unit,
    proceed: () -> Unit = {},
    exoneratePlayer: () -> Unit = {},
    onShowRules: () -> Unit,
    isModerator: Boolean,
    isCourt: Boolean = false,
    announcements: List<Announcement> = emptyList(),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (isModerator) {
                AnimatedContent(targetState = seconder) { seconder ->
                    when {
                        seconder != null -> ButtonToken(
                            modifier = Modifier.fillMaxWidth(),
                            buttonType = ComponentType.Primary,
                            onClick = proceed,
                            enabled = actingPlayers.isEmpty()
                        ) {
                            Text("Proceed")
                        }
                        accused != null -> ButtonToken(
                            modifier = Modifier.fillMaxWidth(),
                            buttonType = ComponentType.Neutral,
                            onClick = exoneratePlayer,
                        ) {
                            Text("Exonerate ${accused.name}")
                        }
                        else -> ButtonToken(
                            modifier = Modifier.fillMaxWidth(),
                            buttonType = ComponentType.Primary,
                            onClick = proceed,
                        ) {
                            Text("Proceed")
                        }
                    }
                }
            } else {
                if (isCourt){
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
                    ) {
                        Text(
                            text = "Do you think ${accused?.name} is guilty?",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start),
                        )

                        ButtonToken(
                            modifier = Modifier.fillMaxWidth(),
                            buttonType = ComponentType.Primary,
                            onClick = onVote,
                        ) {
                            Text("Vote")
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                IconToken(
                    imageVector = Icons.Outlined.AutoStories,
                    buttonType = ComponentType.Inverse,
                    onClick = onShowRules,
                )
            }
        }

        item {
            if (accuser != null && accused != null) {
                AnimatedVisibility(true) {
                    AccusationSection(
                        accuser = accuser,
                        accused = accused,
                        seconder = seconder,
                        myProfileId = myPlayerId,
                        onSecond = onSecond,
                        isModerator = isModerator,
                    )
                }
            }
        }

        item {
            PlayersGrid(
                players = players,
                onSelectPlayer = onSelectPlayer,
                myPlayerId = myPlayerId,
                knownIdentities = knownIdentity,
                actingPlayers = actingPlayers,
                showEmpty = false,
                isModerator = isModerator,
                availableSlots = 0,
            )
        }

        item {
            AnnouncementSection(
                announcements = announcements,
            )
        }
    }
}
