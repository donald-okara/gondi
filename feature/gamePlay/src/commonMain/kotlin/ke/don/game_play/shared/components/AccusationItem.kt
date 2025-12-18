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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.components.profile.PlayerItem
import ke.don.components.profile.ProfilesStacked
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.state.Player
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccusationSection(
    modifier: Modifier = Modifier,
    onSecond: () -> Unit,
    accuser: Player,
    accused: Player,
    isModerator: Boolean,
    seconder: Player?,
    myProfileId: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
    ) {
        val secondaryText = seconder?.let { Resources.Strings.GamePlay.secondsAccusation(it.name) }
            ?: stringResource(Resources.Strings.GamePlay.WAITING_FOR_SECOND)

        Text(
            text = Resources.Strings.GamePlay.accuses(accuser.name, accused.name),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
        )

        Text(
            text = secondaryText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
        )

        AccusationItem(
            accuser = accuser,
            accused = accused,
            seconder = seconder,
            myProfileId = myProfileId,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small,
            ),
        )

        AnimatedVisibility(visible = seconder == null && isModerator.not()) {
            ButtonToken(
                onClick = onSecond,
                buttonType = ComponentType.Inverse,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Resources.Strings.GamePlay.SECOND_THE_ACCUSATION),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun AccusationItem(
    modifier: Modifier = Modifier,
    accuser: Player,
    accused: Player,
    seconder: Player?,
    myProfileId: String,
) {
    FlowRow(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilesStacked(
            primaryPlayer = accuser,
            myProfileId = myProfileId,
            secondaryPlayer = seconder,
        )

        IconToken(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        )

        PlayerItem(
            player = accused,
            actionType = ActionType.NONE,
            isSelected = true,
            enabled = true,
            isMe = myProfileId == accused.id,
        )
    }
}
