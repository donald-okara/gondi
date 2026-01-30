/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.dialog.BottomSheetToken
import ke.don.components.profile.PlayerItem
import ke.don.components.profile.componentType
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.gameplay.actionType
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.shared.components.ActionConfirmation

@Immutable
data class CourtModalStrings(
    val dormantText: String = "",
    val deadPlayerText: String = "",
    val voteInnocentText: String = "",
    val voteGuiltyText: String = "",
    val confirmInnocentText: (String) -> String = { "" },
    val confirmGuiltyText: (String) -> String = { "" },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtModal(
    modifier: Modifier = Modifier,
    onEvent: (PlayerHandler) -> Unit,
    currentPlayer: Player,
    selectedPlayer: Player,
    gameState: GameState,
    strings: CourtModalStrings = CourtModalStrings(),
    vote: Vote? = null,
) {
    var guiltyDecision by remember { mutableStateOf<Boolean?>(null) }

    val confirmationText by remember(guiltyDecision, selectedPlayer) {
        derivedStateOf {
            when (guiltyDecision) {
                true -> strings.confirmGuiltyText(selectedPlayer.name)
                false -> strings.confirmInnocentText(selectedPlayer.name)
                null -> null
            }
        }
    }

    val confirmationAction by remember(
        guiltyDecision,
        selectedPlayer,
        currentPlayer,
        gameState.round,
    ) {
        derivedStateOf {
            guiltyDecision?.let { isGuilty ->
                {
                    onEvent(
                        PlayerHandler.Send(
                            PlayerIntent.Vote(
                                playerId = currentPlayer.id,
                                round = gameState.round,
                                vote = Vote(
                                    voterId = currentPlayer.id,
                                    targetId = selectedPlayer.id,
                                    isGuilty = isGuilty,
                                ),
                            ),
                        ),
                    )
                }
            }
        }
    }

    BottomSheetToken(
        onDismissRequest = {
            guiltyDecision = null
            onEvent(PlayerHandler.ShowVoteDialog)
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerItem(
                player = selectedPlayer,
                onClick = {},
                isSelected = true,
                actionType = currentPlayer.role?.actionType ?: ActionType.NONE,
            )

            ModalActions(
                currentPlayer = currentPlayer,
                vote = { guiltyDecision = it },
                hasVoted = vote != null,
                strings = strings,
            )

            HorizontalDivider()

            AnimatedVisibility(
                visible = guiltyDecision != null,
            ) {
                ActionConfirmation(
                    confirmationText = confirmationText ?: "",
                    componentType = currentPlayer.role?.actionType?.componentType()
                        ?: ComponentType.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                    onConfirm = {
                        confirmationAction?.invoke()
                        onEvent(PlayerHandler.ShowVoteDialog) // dismiss modal
                    },
                    onDismiss = { guiltyDecision = null },
                )
            }
        }
    }
}

@Composable
private fun ModalActions(
    modifier: Modifier = Modifier,
    vote: (Boolean) -> Unit,
    hasVoted: Boolean,
    strings: CourtModalStrings,
    currentPlayer: Player,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.End),
    ) {
        if (hasVoted.not() && currentPlayer.isAlive) {
            ButtonToken(
                buttonType = ComponentType.Primary,
                onClick = { vote(false) },
                enabled = currentPlayer.isAlive,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = null,
                )
                Text(
                    text = strings.voteInnocentText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            ButtonToken(
                buttonType = ComponentType.Error,
                onClick = { vote(true) },
                enabled = currentPlayer.isAlive,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbDown,
                    contentDescription = null,
                )
                Text(
                    text = strings.voteGuiltyText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            Text(
                text = if (currentPlayer.isAlive) strings.dormantText else strings.deadPlayerText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
