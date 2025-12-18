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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ke.don.game_play.shared.components.ActionConfirmation
import ke.don.game_play.shared.components.ModalActions
import ke.don.resources.Resources
import ke.don.utils.formatArgs
import org.jetbrains.compose.resources.stringResource

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

    val gondiConfirmation = stringResource(Resources.Strings.GamePlay.CONFIRMATION_GONDI).formatArgs(selectedPlayer.name)
    val doctorConfirmation = stringResource(Resources.Strings.GamePlay.CONFIRMATION_DOCTOR).formatArgs(selectedPlayer.name)
    val detectiveConfirmation = stringResource(Resources.Strings.GamePlay.CONFIRMATION_DETECTIVE).formatArgs(selectedPlayer.name)

    val confirmationText = remember(currentPlayer.role, selectedPlayer.id) {
        when (currentPlayer.role) {
            Role.GONDI -> gondiConfirmation
            Role.DOCTOR -> doctorConfirmation
            Role.DETECTIVE -> detectiveConfirmation
            else -> null
        }
    }

    val gondiDormant = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_GONDI).formatArgs(selectedPlayer.name)
    val doctorDormant = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DOCTOR).formatArgs(selectedPlayer.name)
    val detectiveDormant = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DETECTIVE).formatArgs(selectedPlayer.name)
    val defaultDormant = stringResource(Resources.Strings.GamePlay.DORMANT_TEXT_DEFAULT).formatArgs(selectedPlayer.name)


    val dormantText = remember(currentPlayer.role) {
        when (currentPlayer.role) {
            Role.GONDI -> gondiDormant
            Role.DOCTOR -> doctorDormant
            Role.DETECTIVE -> detectiveDormant
            else -> defaultDormant
        }
    }

    val confirmationAction: () -> Unit by remember(currentPlayer.role) {
        derivedStateOf {
            when (currentPlayer.role) {
                Role.GONDI -> {
                    {
                        onEvent(
                            PlayerHandler.Send(
                                PlayerIntent.Kill(
                                    currentPlayer.id,
                                    gameState.round,
                                    selectedPlayer.id,
                                ),
                            ),
                        )
                    }
                }

                Role.DOCTOR -> {
                    {
                        onEvent(
                            PlayerHandler.Send(
                                PlayerIntent.Save(
                                    currentPlayer.id,
                                    gameState.round,
                                    selectedPlayer.id,
                                ),
                            ),
                        )
                    }
                }

                Role.DETECTIVE -> {
                    {
                        onEvent(
                            PlayerHandler.Send(
                                PlayerIntent.Investigate(
                                    currentPlayer.id,
                                    gameState.round,
                                    selectedPlayer.id,
                                ),
                            ),
                        )
                    }
                }

                else -> {
                    { } // empty lambda
                }
            }
        }
    }

    BottomSheetToken(
        onDismissRequest = {
            showConfirmation = false
            onEvent(PlayerHandler.SelectPlayer(null))
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
                selectedPlayer = selectedPlayer,
                currentRound = gameState.round,
                currentPlayer = currentPlayer,
                showConfirmation = { showConfirmation = true },
                dormantText = dormantText,
                actionType = currentPlayer.role?.actionType ?: ActionType.NONE,
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
                        onConfirm = {
                            confirmationAction()
                            onEvent(PlayerHandler.SelectPlayer(null)) // dismiss modal
                        },
                        onDismiss = { showConfirmation = false },
                    )
                }
            }
        }
    }
}
