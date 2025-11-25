/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.text_field.TextFieldToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.Role
import ke.don.game_play.moderator.components.RoleConfigurationContainer
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.utils.result.isLoading

@Composable
fun CreateGameContent(
    modifier: Modifier = Modifier,
    state: ModeratorState,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val characterLimit = 20

    val totalPlayers = remember(state.assignment) {
        state.assignment.filterNot {
            it.first == Role.MODERATOR
        }.sumOf { it.second }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextFieldToken(
            text = state.newGame.name,
            onValueChange = { onEvent(ModeratorHandler.UpdateRoomName(it)) },
            label = "Room Name",
            maxLength = characterLimit,
            isError = state.newGame.name.length >= characterLimit,
            errorMessage = "Room name has to be less than $characterLimit characters",
            isRequired = true,
            showLength = true,
        )

        RoleConfigurationContainer(
            state = state,
            onEvent = onEvent,
            totalPlayers = totalPlayers,
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = Theme.typography.bodyMedium.toSpanStyle()) {
                    append("Total players = ")
                }
                withStyle(
                    style = Theme.typography.headlineMedium.toSpanStyle()
                        .copy(fontWeight = FontWeight.Bold),
                ) {
                    append(totalPlayers.toString())
                }
            },
        )

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        ButtonToken(
            modifier = modifier.fillMaxWidth(),
            onClick = { onEvent(ModeratorHandler.StartServer) },
            buttonType = ComponentType.Primary,
            loading = state.createStatus.isLoading,
            enabled = state.assignment.sumOf { it.second } > 0 && state.newGame.name.isNotBlank() && state.createStatus.isLoading.not(),
        ) {
            Text(
                text = "Confirm and Start Game",
            )
        }
    }
}
