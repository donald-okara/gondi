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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ke.don.components.button.ComponentType
import ke.don.components.icon.IconToken
import ke.don.components.indicator.GlowingSelectableSurface
import ke.don.components.indicator.LoadingDotsInCircle
import ke.don.design.theme.AppTheme
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Faction
import ke.don.domain.state.Player
import ke.don.resources.Resources
import ke.don.resources.color
import ke.don.resources.onColor
import ke.don.resources.painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    actionType: ActionType,
    onClick: () -> Unit = {},
    isMe: Boolean = false,
    showRole: Boolean = false,
    isSelected: Boolean,
    isActing: Boolean = false,
    enabled: Boolean = false,
    player: Player,
) {
    // Animate alpha for the entire item if the player is not alive
    val contentAlpha by animateFloatAsState(
        targetValue = if (player.isAlive) 1.0f else 0.5f,
        animationSpec = tween(300),
        label = "playerAlpha",
    )

    val color by animateColorAsState(
        targetValue =
            if (isSelected) {
                actionType.color(
                    default = player.background.color(),
                )
            } else {
                Theme.colorScheme.primary
            },
        animationSpec = tween(300),
    )
    GlowingSelectableSurface(
        modifier = modifier.graphicsLayer { alpha = contentAlpha },
        onClick = onClick,
        selected = isSelected,
        enabled = player.isAlive && enabled,
        glowingColor = color,
        icon = {
            when {
                isActing ->
                    AnimatedVisibility(visible = true) {
                        LoadingDotsInCircle(
                            containerColor = color,
                            dotColor = color.onColor(),
                        )
                    }

                isSelected -> actionType.painter?.let { painter ->
                    AnimatedVisibility(visible = true) { // isSelected is already true here
                        IconToken(
                            painter = painter,
                            colors = IconButtonColors(
                                containerColor = color,
                                contentColor = color.onColor(),
                                disabledContainerColor = color.copy(alpha = 0.5f),
                                disabledContentColor = color.onColor().copy(alpha = 0.5f),
                            ),
                        )
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Avatar Box
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(player.background.color()),
            ) {
                // Player Avatar or Initials
                val imageAlpha =
                    if (player.isAlive) 1f else 0.5f // Grayscale effect is handled by layer
                val imageModifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAlpha)

                player.avatar?.painter()?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = player.name,
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier,
                    )
                } ?: InitialsToken(
                    modifier = imageModifier,
                    profile = player.toProfile(),
                    isHero = true,
                    isSelected = false,
                )

                // Eliminated Overlay
                if (!player.isAlive) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Using the skull icon from your HTML example
                        Icon(
                            painter = painterResource(Resources.Images.ActionIcons.DEAD),
                            contentDescription = "Eliminated",
                            tint = Color.Red.copy(alpha = 0.8f),
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
            }

            // Player Name
            Text(
                text = if (isMe) "You" else player.name,
                style = Theme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Theme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            AnimatedVisibility(
                visible = player.isAlive.not(),
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Theme.colorScheme.errorContainer,
                    contentColor = Theme.colorScheme.onErrorContainer,
                ) {
                    Text(
                        text = "Eliminated",
                        style = Theme.typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.extraSmall,
                            vertical = MaterialTheme.spacing.tiny,
                        ),
                    )
                }
            }
            AnimatedVisibility(
                visible = showRole || player.isAlive.not(),
            ) {
                Text(
                    text = player.role?.name ?: "Waiting",
                    style = Theme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = player.role?.faction?.color ?: Theme.colorScheme.primary,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StackedBase(
    modifier: Modifier = Modifier,
    primary: (@Composable () -> Unit)? = null,
    secondary: (@Composable () -> Unit)? = null,
    spacing: Dp = MaterialTheme.spacing.small,
    overlap: Dp = 80.dp,
) {
    LookaheadScope {
        Row(
            modifier = modifier
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally)
        ) {
            secondary?.let {
                Box(
                    modifier = Modifier
                ) { it() }
            }

            primary?.let {
                Box(
                    modifier = Modifier
                        .then(
                            if (secondary != null)
                                Modifier.offset(x = -(overlap))
                            else Modifier
                        )
                        .zIndex(1f)
                ) { it() }
            }
        }
    }
}



@Composable
fun ProfilesStacked(
    modifier: Modifier = Modifier,
    primaryPlayer: Player?,
    secondaryPlayer: Player?,
) {
    StackedBase(
        modifier = modifier,
        primary = primaryPlayer?.let {
            {
                PlayerItem(
                    player = it,
                    actionType = ActionType.ACCUSE,
                    isSelected = true,
                    enabled = true
                )
            }
        },
        secondary =
            secondaryPlayer?.let {
                {
                    PlayerItem(
                        player = it,
                        actionType = ActionType.SECOND,
                        isSelected = true,
                        enabled = true,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 0.5f
                            }
                    )
                }
            },
    )

}

/**
 * Maps an [ActionType] to a corresponding drawable resource for its icon.
 * This is used to visually represent the action a player is performing or has performed.
 *
 * @return A [DrawableResource] representing the icon for the action, or `null` if the action
 * has no associated icon (e.g., [ActionType.NONE]).
 *
 * NOTE: This property is not currently being used but it is handy to have around. shrinkResources is enabled
 * for release builds.
 */
val ActionType.painter: DrawableResource?
    get() = when (this) {
        ActionType.KILL -> Resources.Images.ActionIcons.DEAD
        ActionType.ACCUSE, ActionType.SECOND -> Resources.Images.ActionIcons.ACCUSE
        ActionType.SAVE -> Resources.Images.ActionIcons.SAVE
        ActionType.INVESTIGATE -> Resources.Images.ActionIcons.INVESTIGATE
        ActionType.NONE -> null
    }

@Composable
fun ActionType.color(
    default: Color = Theme.colorScheme.primary,
): Color = when (this) {
    ActionType.KILL, ActionType.ACCUSE -> Theme.colorScheme.error
    ActionType.SAVE -> AppTheme.extendedColors.success.color
    ActionType.SECOND, ActionType.INVESTIGATE -> AppTheme.extendedColors.warning.color
    ActionType.NONE -> default
}

@Composable
fun ActionType.componentType(
    default: ComponentType = ComponentType.Primary,
): ComponentType = when (this) {
    ActionType.KILL, ActionType.ACCUSE -> ComponentType.Error
    ActionType.SAVE -> ComponentType.Success
    ActionType.SECOND, ActionType.INVESTIGATE -> ComponentType.Warning
    ActionType.NONE -> default
}

val Faction.color: Color
    @Composable get() = when (this) {
        Faction.GONDI -> Theme.colorScheme.error
        Faction.VILLAGER -> AppTheme.extendedColors.success.color
        Faction.NEUTRAL -> Theme.colorScheme.secondary
    }
