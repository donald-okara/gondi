/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.components.card.CardToken
import ke.don.components.card.CardType
import ke.don.components.profile.ProfileImageToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.table.Profile
import ke.don.resources.color

@Composable
fun GameRoomItem(
    modifier: Modifier = Modifier,
    gameIdentity: GameIdentity,
    onClick: () -> Unit,
) {
    val base = CardDefaults.cardColors().containerColor
    val tint = gameIdentity.moderatorAvatarBackground.color()

    val blended = base.copy(
        red = (base.red * 0.9f) + (tint.red * 0.1f),
        green = (base.green * 0.9f) + (tint.green * 0.1f),
        blue = (base.blue * 0.9f) + (tint.blue * 0.1f),
    )

    CardToken(
        modifier = modifier
            .fillMaxWidth(),
        cardType = CardType.Solid,
        colors = CardDefaults.cardColors().copy(containerColor = blended),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(Theme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileImageToken(
                isHero = false,
                profile = Profile(
                    username = gameIdentity.moderatorName,
                    avatar = gameIdentity.moderatorAvatar,
                    background = gameIdentity.moderatorAvatarBackground,
                ),
            )
            Spacer(Modifier.size(Theme.spacing.medium))
            Column {
                Text(
                    text = gameIdentity.gameName,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(Theme.spacing.extraSmall))
                Text(
                    text = "Hosted by ${gameIdentity.moderatorName}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
