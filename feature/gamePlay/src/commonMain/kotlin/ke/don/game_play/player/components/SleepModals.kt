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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.actionType
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepModal(
    modifier: Modifier = Modifier,
    onEvent: (PlayerHandler) -> Unit,
    currentPlayer: Player,
    selectedPlayer: Player,
    gameState: GameState,
) {
    var showConfirmation by remember { mutableStateOf(false) }

    val confirmationText by remember(currentPlayer.role) {
        derivedStateOf {
            when (currentPlayer.role) {
                Role.GONDI -> "Do you want to end ${selectedPlayer.name}'s journey tonight?"
                Role.DOCTOR -> "Will you use your skills to save ${selectedPlayer.name}?"
                Role.DETECTIVE -> "Time to uncover the truth. Investigate ${selectedPlayer.name}?"
                else -> null
            }
        }
    }

    val confirmationAction: () -> Unit by remember(currentPlayer.role) {
        derivedStateOf {
            when (currentPlayer.role) {
                Role.GONDI -> {
                    { onEvent(PlayerHandler.Send(PlayerIntent.Kill(currentPlayer.id, gameState.round, selectedPlayer.id))) }
                }
                Role.DOCTOR -> {
                    { onEvent(PlayerHandler.Send(PlayerIntent.Save(currentPlayer.id, gameState.round, selectedPlayer.id))) }
                }
                Role.DETECTIVE -> {
                    { onEvent(PlayerHandler.Send(PlayerIntent.Investigate(currentPlayer.id, gameState.round, selectedPlayer.id))) }
                }
                else -> {
                    { } // empty lambda
                }
            }
        }
    }

    BottomSheetToken(
        onDismissRequest = { onEvent(PlayerHandler.SelectPlayer(null)) },
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
                selectedPlayer = selectedPlayer,
                currentRound = gameState.round,
                currentPlayer = currentPlayer,
                onEvent = onEvent,
                showConfirmation = { showConfirmation = true },
            )

            HorizontalDivider()

            AnimatedVisibility(
                visible = showConfirmation,
            ) {
                confirmationText?.let {
                    ActionConfirmation(
                        confirmationText = it,
                        componentType = currentPlayer.role?.actionType?.componentType()
                            ?: ComponentType.Neutral,
                        modifier = Modifier.fillMaxWidth(),
                        onConfirm = { confirmationAction() },
                        onDismiss = { showConfirmation = false },
                    )
                }
            }
        }
    }
}

@Composable
private fun ModalActions(
    modifier: Modifier = Modifier,
    onEvent: (PlayerHandler) -> Unit,
    showConfirmation: () -> Unit,
    selectedPlayer: Player,
    currentRound: Long,
    currentPlayer: Player,
) {
    val actionButton = remember(currentPlayer) {
        @Composable {
            when (currentPlayer.role) {
                Role.GONDI -> ButtonToken(
                    buttonType = ComponentType.Error,
                    onClick = {
                        onEvent(
                            PlayerHandler.Send(
                                PlayerIntent.Kill(
                                    currentPlayer.id,
                                    currentRound,
                                    selectedPlayer.id,
                                ),
                            ),
                        )
                    },
                ) {
                    Text(
                        text = "Kill ${selectedPlayer.name}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Role.DOCTOR ->
                    ButtonToken(
                        buttonType = ComponentType.Success,
                        onClick = {
                            onEvent(
                                PlayerHandler.Send(
                                    PlayerIntent.Save(
                                        currentPlayer.id,
                                        currentRound,
                                        selectedPlayer.id,
                                    ),
                                ),
                            )
                        },
                    ) {
                        Text(
                            text = "Save ${selectedPlayer.name}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                Role.DETECTIVE ->
                    ButtonToken(
                        buttonType = ComponentType.Warning,
                        onClick = {
                            onEvent(
                                PlayerHandler.Send(
                                    PlayerIntent.Investigate(
                                        currentPlayer.id,
                                        currentRound,
                                        selectedPlayer.id,
                                    ),
                                ),
                            )
                        },
                    ) {
                        Text(
                            text = "Investigate ${selectedPlayer.name}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                else -> Unit
            }
        }
    }

    val dormantText by remember(currentPlayer.role) {
        derivedStateOf {
            when (currentPlayer.role) {
                Role.GONDI -> "You already killed during this round"
                Role.DOCTOR -> "You already healed during this round"
                Role.DETECTIVE -> "You already investigated during this round"
                else -> "You should get some rest. There is nothing to do"
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.End),
    ) {
        if (currentPlayer.lastAction?.round != currentRound) {
            actionButton.invoke()
        } else {
            Text(
                text = dormantText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ActionConfirmation(
    confirmationText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    componentType: ComponentType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = confirmationText)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonToken(onClick = onDismiss, buttonType = ComponentType.Neutral) {
                Text(text = "Never mind")
            }
            ButtonToken(onClick = onConfirm, buttonType = componentType) {
                Text(text = "I am sure")
            }
        }
    }
}
