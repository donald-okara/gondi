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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ke.don.components.button.ComponentType
import ke.don.components.card.CardToken
import ke.don.components.dialog.DialogToken
import ke.don.components.icon.IconBox
import ke.don.components.icon.IconToken
import ke.don.components.profile.AdaptiveIcon
import ke.don.components.profile.color
import ke.don.design.theme.SpacingType
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Role
import ke.don.resources.Resources
import ke.don.resources.RoleInstruction
import ke.don.resources.VictoryCondition
import ke.don.resources.description
import ke.don.resources.icon
import ke.don.resources.instructions
import ke.don.utils.capitaliseFirst
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
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
            text = stringResource(Resources.Strings.GamePlay.VICTORY_CONDITIONS),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(Resources.Strings.GamePlay.GAME_OBJECTIVE_DESCRIPTION),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        // Replaced LazyVerticalGrid with FlowRow to avoid infinite height exception when nested in a scrollable parent
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize)
                .fillMaxWidth()
        ) {
            item{
                VictoryConditionCard(
                    condition = villagersVictory(),
                )
            }
            item{
                VictoryConditionCard(
                    condition = gondiVictory(),
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

            IconBox(
                icon = condition.icon,
                accentColor = condition.accentColor,
                sizeInt = 64
            )

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
        append(stringResource(Resources.Strings.Guide.THE_VILLAGERS).substringBefore("Villagers"))
        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold)) {
            append(stringResource(Resources.Strings.Guide.UNINFORMED_MAJORITY))
        }
        append(stringResource(Resources.Strings.Guide.VILLAGERS_VICTORY_DESCRIPTION))
    }

    return VictoryCondition(
        title = stringResource(Resources.Strings.Guide.THE_VILLAGERS),
        description = description,
        icon = Resources.Images.LOGO,
        accentColor = accentColor,
        winText = stringResource(Resources.Strings.Guide.ELIMINATE_ALL_GONDI)
    )
}

@Composable
fun gondiVictory(): VictoryCondition {
    val accentColor = Faction.GONDI.color

    val description = buildAnnotatedString {
        append(stringResource(Resources.Strings.Guide.THE_GONDIS).substringBefore("Gondis"))
        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold)) {
            append(stringResource(Resources.Strings.Guide.INFORMED_MINORITY))
        }
        append(stringResource(Resources.Strings.Guide.GONDI_VICTORY_DESCRIPTION))
    }

    return VictoryCondition(
        title = stringResource(Resources.Strings.Guide.THE_GONDIS),
        description = description,
        icon = Resources.Images.RoleIcons.VILLAGER,
        accentColor = accentColor,
        winText = stringResource(Resources.Strings.Guide.EQUAL_NUMBERS)
    )
}


@Composable
fun RolesList(
    modifier: Modifier = Modifier
) {
    val visibleRole = remember {
        mutableStateOf<Role?>(null)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Section Title
        Text(
            text = "Characters & Roles",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(250.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize)
                .fillMaxWidth()
        ) {
            items(Role.entries) { role ->
                RoleCard(role = role){
                    visibleRole.value = role
                }
            }
        }
    }

    visibleRole.value?.let { role ->
        RoleDialog(
            role = role,
            dismiss = { visibleRole.value = null }
        )
    }
}



@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    role: Role,
    onRoleClick: (Role) -> Unit = {}
) {
    val accentColor = role.faction.color

    CardToken(
        modifier = modifier.fillMaxWidth(),
        onClick = { onRoleClick(role) },
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(24.dp)
        ) {
            IconBox(
                icon = role.icon,
                accentColor = accentColor,
                sizeInt = 64
            )

            Spacer(Modifier.height(12.dp))

            Text(text = role.name.capitaliseFirst(), fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(4.dp))

            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }

}


@Composable
fun RoleDialog(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
    role: Role,
) {
    val accentColor = role.faction.color

    DialogToken(
        modifier = modifier,
        onDismissRequest = dismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconBox(
                icon = role.icon,
                accentColor = accentColor,
                sizeInt = 128
            )

            Text(
                text = role.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Theme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = role.description,
                style = MaterialTheme.typography.titleSmall,
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
        IconBox(
            icon = instruction.icon,
            accentColor = MaterialTheme.colorScheme.primary,
            sizeInt = 48
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