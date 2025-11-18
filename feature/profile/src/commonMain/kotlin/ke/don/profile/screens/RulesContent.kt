/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.list_items.CodeOfConductSection
import ke.don.components.list_items.GamePhases
import ke.don.components.list_items.RolesList
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

@Composable
fun RulesContent(
    modifier: Modifier = Modifier,
    next: () -> Unit,
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

        CodeOfConductSection()

        RolesList()

        Spacer(modifier = Modifier.height(8.dp))

        ButtonToken(
            modifier = Modifier.align(Alignment.End),
            buttonType = ComponentType.Primary,
            onClick = next,
        ) {
            Text("I understand")
        }
    }
}

@Composable
fun PhasesContent(
    modifier: Modifier = Modifier,
    next: () -> Unit,
    back: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.Top),
    ) {
        GamePhases()

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonToken(
                buttonType = ComponentType.Neutral,
                onClick = back,
            ) {
                Text("Back to rules")
            }

            ButtonToken(
                buttonType = ComponentType.Primary,
                onClick = next,
            ) {
                Text("Set up profile")
            }
        }
    }
}
