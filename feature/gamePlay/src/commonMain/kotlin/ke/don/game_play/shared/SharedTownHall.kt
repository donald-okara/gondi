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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import ke.don.game_play.moderator.model.Announcement
import ke.don.game_play.shared.components.AccusationSection
import ke.don.game_play.shared.components.AnnouncementSection
import ke.don.game_play.shared.components.PlayersGrid
import ke.don.game_play.shared.components.RevealDeathModal
import ke.don.game_play.shared.components.RevealDeathsStrings
import kotlin.time.ExperimentalTime

@Immutable
data class SharedTownHallStrings(
    val sessionOver: String = "",
    val proceed: String = "",
    val goToCourt: String = "",
    val noAccusations: String = "",
    val vote: String = "",
    val noSeconder: (String) -> String = { "" },
    val exoneratePlayer: (String) -> String = { "" },
    val isPlayerGuilty: (String) -> String = { "" },
)

@OptIn(ExperimentalTime::class)
@Composable
fun SharedTownHall(
    players: List<Player>,
    revealDeaths: Boolean,
    onDismiss: () -> Unit,
    lastSaved: String?,
    lastKilled: List<String>,
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
    townHallStrings: SharedTownHallStrings = SharedTownHallStrings(),
    revealDeathsStrings: RevealDeathsStrings = RevealDeathsStrings(),
) {
    val savedPlayer by remember(players, lastSaved) {
        derivedStateOf { players.find { player -> player.id == lastSaved } }
    }
    val killedPlayers by remember(players, lastKilled) {
        derivedStateOf { players.filter { player -> lastKilled.contains(player.id) && player.id != lastSaved } }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (isModerator) {
                AnimatedContent(targetState = seconder) { seconder ->
                    when {
                        isCourt ->
                            CallToActionSection(
                                modifier = Modifier,
                                explanationText = townHallStrings.sessionOver,
                                callToActionText = townHallStrings.proceed,
                                componentType = ComponentType.Primary,
                                onActionClick = proceed,
                                enabled = actingPlayers.isEmpty(),
                            )

                        seconder != null ->
                            CallToActionSection(
                                modifier = Modifier,
                                explanationText = townHallStrings.goToCourt,
                                callToActionText = townHallStrings.proceed,
                                componentType = ComponentType.Primary,
                                onActionClick = proceed,
                                enabled = true,
                            )

                        accused != null ->
                            CallToActionSection(
                                modifier = Modifier,
                                explanationText = townHallStrings.noSeconder(accused.name),
                                callToActionText = townHallStrings.exoneratePlayer(accused.name),
                                componentType = ComponentType.Primary,
                                onActionClick = exoneratePlayer,
                                enabled = true,
                            )

                        else ->
                            CallToActionSection(
                                modifier = Modifier,
                                explanationText = townHallStrings.noAccusations,
                                callToActionText = townHallStrings.proceed,
                                componentType = ComponentType.Primary,
                                onActionClick = proceed,
                                enabled = actingPlayers.isEmpty(),
                            )
                    }
                }
            } else {
                if (isCourt && accused != null) {
                    CallToActionSection(
                        modifier = Modifier,
                        explanationText = townHallStrings.isPlayerGuilty(accused.name),
                        callToActionText = townHallStrings.vote,
                        componentType = ComponentType.Primary,
                        onActionClick = onVote,
                        enabled = true,
                    )
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

    if (revealDeaths) {
        RevealDeathModal(
            onDismiss = onDismiss,
            savedPlayer = savedPlayer,
            killedPlayers = killedPlayers,
            currentPhase = GamePhase.TOWN_HALL,
            strings = revealDeathsStrings,
        )
    }
}

@Composable
fun CallToActionSection(
    modifier: Modifier = Modifier,
    explanationText: String,
    callToActionText: String,
    componentType: ComponentType,
    onActionClick: () -> Unit,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        // --- 1. Explanation Text ---
        Text(
            text = explanationText,
            style = MaterialTheme.typography.titleMedium,
        )

        // --- 2. Call to Action Button ---
        ButtonToken(
            modifier = Modifier.fillMaxWidth(),
            buttonType = componentType,
            enabled = enabled,
            onClick = onActionClick,
        ) {
            Text(callToActionText)
        }
    }
}
