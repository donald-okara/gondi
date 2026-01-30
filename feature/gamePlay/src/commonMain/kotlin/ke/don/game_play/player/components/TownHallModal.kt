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
import androidx.compose.runtime.Immutable
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
import ke.don.domain.gameplay.actionType
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.shared.components.ActionConfirmation
import ke.don.game_play.shared.components.ModalActions

@Immutable
data class TownHallModalStrings(
    val alreadyOnTrial: String = "",
    val accusePlayerConfirmation: (String) -> String = { "" },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TownHallModal(
    modifier: Modifier = Modifier,
    onEvent: (PlayerHandler) -> Unit,
    currentPlayer: Player,
    selectedPlayer: Player,
    gameState: GameState,
    strings: TownHallModalStrings = TownHallModalStrings(),
) {
    var showConfirmation by remember { mutableStateOf(false) }

    val confirmationText = strings.accusePlayerConfirmation(selectedPlayer.name)
    val confirmationAction = {
        onEvent(
            PlayerHandler.Send(
                PlayerIntent.Accuse(
                    currentPlayer.id,
                    gameState.round,
                    selectedPlayer.id,
                ),
            ),
        )
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
                dormantText = strings.alreadyOnTrial,
                actionType = ActionType.ACCUSE,
            )

            HorizontalDivider()

            AnimatedVisibility(
                visible = showConfirmation,
            ) {
                ActionConfirmation(
                    confirmationText = confirmationText,
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
