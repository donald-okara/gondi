/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.domain.table.Profile
import ke.don.resources.color
import ke.don.resources.onColorWithOverlay
import ke.don.utils.getInitials

@Composable
fun InitialsToken(
    profile: Profile,
    isHero: Boolean = false,
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 2f else 1f,
        label = "Initials scale"
    )


    val baseTextStyle = if (isHero) {
        MaterialTheme.typography.headlineMedium
    } else {
        MaterialTheme.typography.labelMedium
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = profile.username.getInitials(),
            style = baseTextStyle.copy(
                fontWeight = FontWeight.Bold,
                color = profile.background.color().onColorWithOverlay(),
            ),
            modifier = Modifier.scale(scale),
            maxLines = 1,
        )
    }
}
