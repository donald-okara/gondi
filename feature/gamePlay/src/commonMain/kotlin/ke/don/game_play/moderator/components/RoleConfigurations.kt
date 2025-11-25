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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.components.profile.AdaptiveIcon
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.Role
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.moderator.model.RoleAssignment
import ke.don.game_play.moderator.useCases.PLAYER_DET_LIMIT
import ke.don.resources.icon
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun RoleConfigurationContainer(
    modifier: Modifier = Modifier,
    state: ModeratorState,
    onEvent: (ModeratorHandler) -> Unit,
    totalPlayers: Int,
) {
    val baseColor = MaterialTheme.colorScheme.primary

    val containerAlpha = 0.1f
    val borderAlpha = 0.5f

    Surface(
        shape = Theme.shapes.medium,
        color = baseColor.copy(alpha = containerAlpha),
        tonalElevation = Theme.spacing.small,
        border = BorderStroke(
            width = Theme.spacing.tiny,
            color = baseColor.copy(alpha = borderAlpha),
        ),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        RoleConfigurationList(
            state = state,
            onEvent = onEvent,
            totalPlayers = totalPlayers,
        )
    }
}

@Composable
fun RoleConfigurationList(
    modifier: Modifier = Modifier,
    state: ModeratorState,
    onEvent: (ModeratorHandler) -> Unit,
    totalPlayers: Int,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = Theme.spacing.medium,
                vertical = Theme.spacing.large,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
    ) {
        Text(
            text = "Player Roles Configuration",
            style = Theme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.onSurface,
        )
        Text(
            text = "Set the number of players for each role",
            style = Theme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.onSurfaceVariant,
        )

        HorizontalDivider()

        state.assignment.filterNot {
            it.first == Role.MODERATOR
        }.forEach { assignment ->
            RoleConfigurationItem(
                roleAssignment = assignment,
                onAssignmentChange = { onEvent(ModeratorHandler.UpdateAssignments(it)) },
                totalPlayers = totalPlayers,
            )
        }
    }
}

@Composable
fun RoleConfigurationItem(
    modifier: Modifier = Modifier,
    roleAssignment: RoleAssignment,
    onAssignmentChange: (RoleAssignment) -> Unit,
    totalPlayers: Int,
) {
    val maxCount by derivedStateOf {
        when (roleAssignment.first) {
            Role.DOCTOR -> 1
            Role.GONDI -> 2
            Role.DETECTIVE, Role.ACCOMPLICE -> if (totalPlayers < PLAYER_DET_LIMIT) 0 else 1
            else -> 10
        }
    }

    val warningMessage by derivedStateOf {
        when (roleAssignment.first) {
            Role.DETECTIVE, Role.ACCOMPLICE ->
                if (totalPlayers < PLAYER_DET_LIMIT && roleAssignment.second > 0) {
                    "Detective and Accomplice cannot exist in a game with less than $PLAYER_DET_LIMIT players"
                } else if (roleAssignment.second > 1) {
                    "Only one Detective or Accomplice allowed"
                } else {
                    null
                }
            else -> if (roleAssignment.second > maxCount) "Max allowed: $maxCount" else null
        }
    }

    val enableIncrement by derivedStateOf {
        roleAssignment.second < maxCount
    }
    val enableDecrement by derivedStateOf {
        val lowerLimit = if (roleAssignment.first == Role.GONDI) 1 else 0

        roleAssignment.second > lowerLimit
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        RoleConfigurationBase(
            icon = roleAssignment.first.icon,
            label = roleAssignment.first.name,
            count = roleAssignment.second,
            modifier = modifier,
            enableIncrement = enableIncrement,
            enableDecrement = enableDecrement,
            onIncrement = {
                if (roleAssignment.second < maxCount) {
                    onAssignmentChange(roleAssignment.copy(second = roleAssignment.second + 1))
                }
            },
            onDecrement = {
                if (roleAssignment.second > 0) {
                    onAssignmentChange(roleAssignment.copy(second = roleAssignment.second - 1))
                }
            },
        )
        AnimatedVisibility(warningMessage != null) {
            Text(
                text = warningMessage ?: "",
                color = Theme.colorScheme.error.copy(alpha = 0.8f),
                style = Theme.typography.bodySmall,
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun RoleConfigurationBase(
    icon: DrawableResource,
    label: String,
    count: Int,
    enableIncrement: Boolean,
    enableDecrement: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        ) {
            Box(
                modifier = Modifier
                    .size(Theme.spacing.extraLarge)
                    .background(Theme.colorScheme.primary.copy(alpha = 0.1f), Theme.shapes.medium),
                contentAlignment = Alignment.Center,
            ) {
                AdaptiveIcon(
                    modifier = Modifier
                        .padding(Theme.spacing.extraSmall)
                        .fillMaxSize(),
                    painterResource = icon,
                    contentDescription = null,
                    logoColor = Theme.colorScheme.primary,
                )
            }

            Text(
                text = label,
                style = Theme.typography.bodyLarge.copy(
                    color = Theme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                ),
                maxLines = 1,
            )
        }

        // Counter Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        ) {
            IconToken(
                onClick = onDecrement,
                imageVector = Icons.Default.Remove,
                buttonType = ComponentType.Neutral,
                enabled = enableDecrement,
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Theme.colorScheme.onBackground,
                ),
            )

            IconToken(
                onClick = onIncrement,
                enabled = enableIncrement,
                imageVector = Icons.Default.Add,
                buttonType = ComponentType.Neutral,
            )
        }
    }
}
