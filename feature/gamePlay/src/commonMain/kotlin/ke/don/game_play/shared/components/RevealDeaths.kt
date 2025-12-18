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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.dialog.DialogToken
import ke.don.components.profile.ProfileImageToken
import ke.don.components.profile.color
import ke.don.components.profile.painter
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.state.GamePhase
import ke.don.domain.state.Player
import ke.don.utils.capitaliseFirst
import org.jetbrains.compose.resources.painterResource

@Immutable
data class RevealDeathsStrings(
    val nightResultsTitle: String,
    val courtRulingTitle: String,
    val nightResultsDescription: String,
    val courtRulingDescription: String,
    val killedPlayerContentDescription: String,
    val savedPlayerContentDescription: String,
    val eliminatedPlayerMessage: (String) -> String,
    val savedBySaviourMessage: (String) -> String,
    val courtRulingText: String,
    val theDoctorText: String,
)

@Composable
fun RevealDeathModal(
    modifier: Modifier = Modifier,
    savedPlayer: Player?,
    currentPhase: GamePhase,
    killedPlayers: List<Player>,
    strings: RevealDeathsStrings,
    onDismiss: () -> Unit,
) {
    DialogToken(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        RevealDeathsComponent(
            savedPlayer = savedPlayer,
            currentPhase = currentPhase,
            killedPlayers = killedPlayers,
            strings = strings,
        )
    }
}

@Composable
fun RevealDeathsComponent(
    modifier: Modifier = Modifier,
    savedPlayer: Player? = null,
    currentPhase: GamePhase,
    killedPlayers: List<Player> = emptyList(),
    strings: RevealDeathsStrings,
) {
    val icon = if (currentPhase == GamePhase.TOWN_HALL) Icons.Default.Bedtime else Icons.Default.Gavel
    val title = if (currentPhase == GamePhase.TOWN_HALL) strings.nightResultsTitle else strings.courtRulingTitle
    val message = if (currentPhase == GamePhase.TOWN_HALL) strings.nightResultsDescription else strings.courtRulingDescription

    Column(
        modifier = modifier
            .padding(16.dp)
            .width(500.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(0.2f))
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.surface,
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Theme.colorScheme.primary,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(500.dp),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
        ) {
            killedPlayers.forEach {
                NightResultItem(
                    player = it,
                    isSleep = currentPhase == GamePhase.SLEEP,
                    status = NightStatus.Killed,
                    strings = strings,
                )
            }
            savedPlayer?.let {
                NightResultItem(
                    player = it,
                    isSleep = currentPhase == GamePhase.SLEEP,
                    status = NightStatus.Saved,
                    strings = strings,
                )
            }
        }
    }
}

@Composable
fun NightResultItem(
    modifier: Modifier = Modifier,
    player: Player,
    isSleep: Boolean,
    status: NightStatus,
    strings: RevealDeathsStrings,
) {
    val action = when (status) {
        NightStatus.Killed -> ActionType.KILL
        NightStatus.Saved -> ActionType.SAVE
    }

    val bg = when (status) {
        NightStatus.Killed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.07f)
        NightStatus.Saved -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    }

    val border = when (status) {
        NightStatus.Saved -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        NightStatus.Killed -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        bg,
                        bg.copy(alpha = 0.55f),
                    ),
                ),
            )
            .border(
                1.dp,
                border,
                MaterialTheme.shapes.large,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(modifier = Modifier.size(56.dp)) {
                ProfileImageToken(
                    isHero = false,
                    isSelected = true,
                    profile = player.toProfile(),
                    modifier = Modifier
                        .matchParentSize(),
                )

                // Status badge
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.surface,
                            CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(action.painter!!),
                        contentDescription = when (status) {
                            NightStatus.Killed -> strings.killedPlayerContentDescription
                            NightStatus.Saved -> strings.savedPlayerContentDescription
                        },
                        tint = action.color(),
                        modifier = Modifier.size(14.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(action.painter!!),
                        contentDescription = null,
                        tint = action.color(),
                        modifier = Modifier.size(16.dp),
                    )

                    val saviour = if (isSleep) strings.courtRulingText else strings.theDoctorText
                    Text(
                        text = when (status) {
                            NightStatus.Killed -> strings.eliminatedPlayerMessage(player.role?.name?.capitaliseFirst() ?: "")
                            NightStatus.Saved -> strings.savedBySaviourMessage(saviour)
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = action.color().copy(alpha = 0.9f),
                        ),
                    )
                }
            }
        }

        // Right-side supporting icon (balanced visual weight)
        if (status == NightStatus.Saved) {
            Icon(
                painter = painterResource(ActionType.SAVE.painter!!),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f),
                modifier = Modifier.size(26.dp),
            )
        }
    }
}

enum class NightStatus { Killed, Saved }
