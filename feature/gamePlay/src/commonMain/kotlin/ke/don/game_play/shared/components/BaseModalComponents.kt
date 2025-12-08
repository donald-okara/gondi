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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.profile.componentType
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.state.Player
import ke.don.utils.capitaliseFirst

@Composable
fun ModalActions(
    modifier: Modifier = Modifier,
    showConfirmation: () -> Unit,
    actionType: ActionType,
    selectedPlayer: Player,
    overrideShowButton: Boolean = false, // For special cases of when the conditions present to show buttons should be ignored.
    currentRound: Long,
    dormantText: String,
    currentPlayer: Player,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.End),
    ) {
        if (
            currentPlayer.lastAction?.round != currentRound &&
            currentPlayer.isAlive &&
            currentPlayer.lastAction?.type != actionType &&
            overrideShowButton.not()
        ) {
            ButtonToken(
                buttonType = actionType.componentType(),
                onClick = showConfirmation,
                enabled = currentPlayer.isAlive,
            ) {
                Text(
                    text = "${actionType.name.capitaliseFirst()} ${selectedPlayer.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            Text(
                text = if (currentPlayer.isAlive) dormantText else "Dead men tell no tales",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun ActionConfirmation(
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
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium,
                Alignment.End,
            ),
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
