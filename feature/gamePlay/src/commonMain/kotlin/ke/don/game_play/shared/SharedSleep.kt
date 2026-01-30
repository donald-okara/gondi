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

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.SelectedPlayer
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import ke.don.game_play.shared.components.PlayersGrid
import ke.don.game_play.shared.components.RevealDeathModal
import ke.don.game_play.shared.components.RevealDeathsStrings

@Immutable
data class SharedSleepStrings(
    val proceed: String = "",
    val showRules: String = "",
)

@Composable
fun SharedSleep(
    modifier: Modifier = Modifier,
    myPlayerId: String?,
    revealDeaths: Boolean,
    onDismiss: () -> Unit,
    lastAccused: String?,
    instruction: String,
    isModerator: Boolean,
    onProceed: () -> Unit,
    onShowRules: () -> Unit,
    enabled: Boolean = false,
    actingPlayers: List<String> = emptyList(),
    knownIdentity: List<String> = emptyList(),
    onSelectPlayer: (String) -> Unit,
    players: List<Player>,
    selectedPlayers: List<SelectedPlayer> = emptyList(),
    strings: SharedSleepStrings = SharedSleepStrings(),
    revealDeathsStrings: RevealDeathsStrings = RevealDeathsStrings(),
) {
    val lastAccusedPlayer by remember(lastAccused, players) {
        derivedStateOf { lastAccused?.let { accusedId -> players.find { it.id == accusedId } } }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (isModerator) {
                ButtonToken(
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = ComponentType.Primary,
                    onClick = onProceed,
                    enabled = enabled,
                ) {
                    Text(
                        strings.proceed,
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
                    contentDescription = strings.showRules,
                    buttonType = ComponentType.Inverse,
                    onClick = onShowRules,
                )
            }
        }
        item {
            Text(
                text = instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            PlayersGrid(
                players = players,
                selectedPlayers = selectedPlayers,
                onSelectPlayer = onSelectPlayer,
                myPlayerId = myPlayerId,
                actingPlayers = actingPlayers,
                knownIdentities = knownIdentity,
                showEmpty = false,
                isModerator = isModerator,
                availableSlots = 0,
            )
        }
    }

    if (revealDeaths) {
        RevealDeathModal(
            onDismiss = onDismiss,
            savedPlayer = if (lastAccusedPlayer?.isAlive == true) lastAccusedPlayer else null,
            killedPlayers = if (lastAccusedPlayer?.isAlive == false && lastAccusedPlayer != null) listOf(lastAccusedPlayer!!) else emptyList(),
            currentPhase = GamePhase.TOWN_HALL,
            strings = revealDeathsStrings,
        )
    }
}
