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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.Faction
import ke.don.domain.state.Player
import ke.don.game_play.shared.components.GameOverGrid

@Immutable
data class SharedGameOverStrings(
    val gondiWinRemark: String,
    val villagerWinRemark: String,
    val gondiWin: String,
    val villagersWin: String,
    val playAgain: String,
)

@Composable
fun SharedGameOver(
    modifier: Modifier = Modifier,
    isModerator: Boolean,
    players: List<Player>,
    myPlayer: Player,
    winnerFaction: Faction,
    playAgain: () -> Unit = {},
    strings: SharedGameOverStrings,
) {
    val remark = remember(winnerFaction) {
        when (winnerFaction) {
            Faction.GONDI -> strings.gondiWinRemark
            Faction.VILLAGER -> strings.villagerWinRemark
            Faction.NEUTRAL -> ""
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
            ) {
                Text(
                    text = when (winnerFaction) {
                        Faction.GONDI -> strings.gondiWin
                        Faction.VILLAGER -> strings.villagersWin
                        Faction.NEUTRAL -> ""
                    },
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (winnerFaction == Faction.GONDI) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = remark,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            if (isModerator) {
                ButtonToken(
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = if (winnerFaction == Faction.GONDI) ComponentType.Error else ComponentType.Primary,
                    onClick = playAgain,
                ) {
                    Text(strings.playAgain)
                }
            }
        }

        item {
            GameOverGrid(
                players = players,
                myPlayerId = myPlayer.id,
                winningFaction = winnerFaction,
            )
        }
    }
}
