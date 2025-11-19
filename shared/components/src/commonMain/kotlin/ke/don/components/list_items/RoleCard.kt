/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.list_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.button.ComponentType
import ke.don.components.card.CardToken
import ke.don.components.card.CardType
import ke.don.components.dialog.DialogToken
import ke.don.components.icon.IconToken
import ke.don.components.profile.AdaptiveIcon
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.SpacingType
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import ke.don.domain.gameplay.Role
import ke.don.resources.RoleInstruction
import ke.don.resources.description
import ke.don.resources.icon
import ke.don.resources.instructions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameObjective(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.Top),
    ) {
        Text(
            text = "Game Objective",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            "The game is a battle between two groups: the Villagers (the uninformed majority) and the Gondi (the informed minority). The Villagers win if they eliminate all Gondi members. The Gondi win when their numbers equal the number of Villagers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun RolesList(modifier: Modifier = Modifier) {
    val spacing = MaterialTheme.spacing.medium
    val minSize = 240.dp

    var showDialog by remember { mutableStateOf<Role?>(null) }

    BoxWithConstraints(modifier) {
        val cols = ((maxWidth - spacing * 2) / minSize)
            .toInt()
            .coerceIn(1, 3) // min 1, max 3 columns

        val itemWidth = (maxWidth - spacing * (cols - 1)) / cols

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            Role.entries.forEach { role ->
                RoleCard(
                    role = role,
                    modifier = Modifier.width(itemWidth),
                    onClick = { showDialog = role },
                )
            }
        }
    }

    showDialog?.let {
        RoleDialog(
            dismiss = { showDialog = null },
            role = it,
        )
    }
}

@Composable
fun RoleDialog(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
    role: Role,
) {
    DialogToken(
        modifier = modifier,
        onDismissRequest = dismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            AdaptiveIcon(
                painterResource = role.icon,
                contentDescription = null,
                logoColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp),
            )

            Text(
                text = role.name,
                style = MaterialTheme.typography.titleMedium,
                color = Theme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Theme.colorScheme.onSurface,
            )

            HorizontalDivider()

            role.instructions.forEach {
                RoleInstructionItem(
                    instruction = it,
                )
            }
        }
    }
}

@Composable
private fun RoleInstructionItem(
    modifier: Modifier = Modifier,
    instruction: RoleInstruction,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.Top,
    ) {
        IconToken(
            buttonType = ComponentType.Neutral,
            imageVector = instruction.icon,
            paddingValues = spacingPaddingValues(
                type = SpacingType.Small,
            ),
            onClick = {},
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = instruction.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = instruction.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    role: Role,
) {
    CardToken(
        cardType = CardType.Solid,
        onClick = onClick,
    ) {
        Column(
            modifier = modifier
                .padding(
                    spacingPaddingValues(
                        type = SpacingType.Medium,
                        vertical = PaddingOption.Default,
                        horizontal = PaddingOption.Custom(
                            MaterialTheme.spacing.small,
                        ),
                    ),
                ),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium,
                Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.Start,
        ) {
            TitleRow(
                icon = role.icon,
                title = role.name,
            )

            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun TitleRow(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    title: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AdaptiveIcon(
            painterResource = icon,
            contentDescription = null,
            logoColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(
                MaterialTheme.spacing.large,
            ),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
    }
}
