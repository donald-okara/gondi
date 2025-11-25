/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.dialog.BottomSheetToken
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.utils.capitaliseFirst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedPlayerModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAssignPlayer: (Role) -> Unit,
    onRemovePlayer: () -> Unit,
    player: Player,
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showAssignDialog by remember { mutableStateOf(false) }
    var selectedRole by remember(player.role) { mutableStateOf(player.role) }

    BottomSheetToken(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerItem(
                player = player,
                onClick = {},
                isSelected = true,
                actionType = ActionType.NONE,
            )

            ModalActions(
                onRemoveClick = {
                    showRemoveDialog = true
                    showAssignDialog = false
                },
                onAssignClick = {
                    showRemoveDialog = false
                    showAssignDialog = true
                },
            )

            HorizontalDivider()

            AnimatedVisibility(
                visible = showRemoveDialog,
            ) {
                RemovePlayerConfirmation(
                    playerName = player.name,
                    onConfirm = {
                        showRemoveDialog = false
                        onRemovePlayer()
                        onDismissRequest()
                    },
                    onDismiss = { showRemoveDialog = false },
                )
            }

            AnimatedVisibility(visible = showAssignDialog) {
                AssignRoleContent(
                    selectedRole = selectedRole,
                    onRoleSelected = { selectedRole = it },
                )
            }
        }
    }

    LaunchedEffect(selectedRole) {
        selectedRole?.let {
            if (it != player.role) {
                onAssignPlayer(it)
                onDismissRequest()
            }
        }
    }
}

@Composable
private fun ModalActions(
    onRemoveClick: () -> Unit,
    onAssignClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ButtonToken(
            onClick = onRemoveClick,
            buttonType = ComponentType.Error,
        ) {
            Icon(Icons.Default.PersonRemove, contentDescription = null)
            Text(text = "Remove")
        }

        ButtonToken(
            onClick = onAssignClick,
            buttonType = ComponentType.Inverse,
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null)
            Text(text = "Assign Role")
        }
    }
}

@Composable
private fun RemovePlayerConfirmation(
    playerName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Are you sure you want to remove $playerName from the game?")
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonToken(onClick = onDismiss, buttonType = ComponentType.Neutral) {
                Text(text = "Never mind")
            }
            ButtonToken(onClick = onConfirm, buttonType = ComponentType.Error) {
                Text(text = "I am sure")
            }
        }
    }
}

@Composable
private fun AssignRoleContent(
    selectedRole: Role?,
    onRoleSelected: (Role) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            Role.entries.filterNot { it == Role.MODERATOR }.forEach { role ->
                RoleChip(
                    role = role,
                    isSelected = role == selectedRole,
                    onRoleSelected = { onRoleSelected(role) },
                )
            }
        }
    }
}

@Composable
private fun RoleChip(
    role: Role,
    isSelected: Boolean,
    onRoleSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animationSpec = tween<Color>(durationMillis = 300)

    val targetContentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val animatedContentColor by animateColorAsState(targetValue = targetContentColor, animationSpec = animationSpec)

    val targetBorderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val animatedBorderColor by animateColorAsState(targetValue = targetBorderColor, animationSpec = animationSpec)

    Surface(
        onClick = onRoleSelected,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = animatedContentColor,
        modifier = modifier.border(
            width = MaterialTheme.spacing.tiny,
            color = animatedBorderColor,
            shape = MaterialTheme.shapes.medium,
        ),
    ) {
        Text(
            text = role.name.capitaliseFirst(),
            modifier = Modifier.padding(MaterialTheme.spacing.small),
        )
    }
}
