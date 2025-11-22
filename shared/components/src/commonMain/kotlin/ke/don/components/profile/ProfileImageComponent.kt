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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.domain.table.Profile
import ke.don.resources.color
import ke.don.resources.painter
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileImageToken(
    modifier: Modifier = Modifier,
    profile: Profile,
    onClick: (() -> Unit)? = null,
    isHero: Boolean,
    isSelected: Boolean = false,
    heroSize: Dp = 96.dp,
    iconSize: Dp = 32.dp,
) {
    ProfileBackground(
        modifier = modifier,
        isHero = isHero,
        onClick = onClick,
        color = profile.background.color(),
        isSelected = isSelected,
        heroSize = heroSize,
        iconSize = iconSize,
    ) {
        profile.avatar?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(profile.avatar!!.painter()),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxSize(),
                )
            }
        } ?: InitialsToken(
            profile = profile,
            isHero = isHero,
            modifier = Modifier,
            isSelected = isSelected,
        )
    }
}

@Composable
fun ProfileBackground(
    modifier: Modifier = Modifier,
    color: Color,
    isHero: Boolean = true,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    heroSize: Dp = 96.dp,
    iconSize: Dp = 32.dp,
    content: @Composable () -> Unit,
) {
    val baseSize = if (isHero) heroSize else iconSize
    val targetSize = if (isSelected) baseSize * 1.5f else baseSize
    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        label = "animatedSize",
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        label = "animatedColor",
    )

    if (onClick != null) {
        Surface(
            color = animatedColor,
            shape = MaterialTheme.shapes.medium,
            onClick = onClick,
            tonalElevation = if (isHero) 3.dp else 1.dp,
            modifier = modifier.size(animatedSize),
        ) {
            content.invoke()
        }
    } else {
        Surface(
            color = animatedColor,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = if (isHero) 3.dp else 1.dp,
            modifier = modifier.size(animatedSize),
        ) {
            content.invoke()
        }
    }
}
