/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.guide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ke.don.components.button.ComponentType
import ke.don.components.card.CardToken
import ke.don.components.card.CardType
import ke.don.components.dialog.DialogToken
import ke.don.components.icon.IconToken
import ke.don.components.profile.AdaptiveIcon
import ke.don.components.profile.color
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.SpacingType
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.Role.entries
import ke.don.resources.Resources
import ke.don.resources.Resources.Strings.GamePlay.VICTORY_CONDITIONS
import ke.don.resources.RoleInstruction
import ke.don.resources.VictoryCondition
import ke.don.resources.description
import ke.don.resources.icon
import ke.don.resources.instructions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun VictoryConditionsSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Victory Conditions",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Gondi is a social deduction game of survival and deception.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize),
            columns = GridCells.Adaptive(240.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                VictoryConditionCard(
                    condition = villagersVictory()
                )
            }
            item {
                VictoryConditionCard(
                    condition = gondiVictory()
                )
            }
        }
    }
}

@Composable
fun VictoryConditionCard(
    condition: VictoryCondition,
    modifier: Modifier = Modifier
) {
    CardToken(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(Modifier.padding(24.dp)) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        condition.accentColor.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AdaptiveIcon(
                    painterResource = condition.icon,
                    contentDescription = null,
                    logoColor = condition.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = condition.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = condition.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(32.dp)
                        .background(condition.accentColor, RoundedCornerShape(50))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = condition.winText,
                    fontWeight = FontWeight.Bold,
                    color = condition.accentColor
                )
            }
        }
    }
}

@Composable
fun villagersVictory(): VictoryCondition {
    val accentColor = Faction.VILLAGER.color

    val description = buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold)) {
            append("uninformed majority")
        }
        append(". They win if they successfully identify and eliminate all Gondi members through logical deduction and voting.")
    }

    return VictoryCondition(
        title = "The Villagers",
        description = description,
        icon = Resources.Images.LOGO,
        accentColor = accentColor,
        winText = "Eliminate all Gondi"
    )
}

@Composable
fun gondiVictory(): VictoryCondition {
    val accentColor = Faction.GONDI.color

    val description = buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold)) {
            append("informed minority")
        }
        append(". They win when they reduce the number of Villagers until it equals the number of Gondi remaining.")
    }

    return VictoryCondition(
        title = "The Gondi",
        description = description,
        icon = Resources.Images.RoleIcons.VILLAGER,
        accentColor = accentColor,
        winText = "Equal numbers"
    )
}


@Composable
fun RolesList(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf<Role?>(null) }

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize),
        columns = GridCells.Adaptive(240.dp),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        contentPadding = PaddingValues(vertical = Theme.spacing.small),
    ) {
        items(entries) { role ->
            RoleCard(
                role = role,
                onClick = { showDialog = role },
            )
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
