/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.design_system.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private const val disabledAlpha = 0.38f

// Map the type to target colors
private val ComponentType.targetContainerColor @Composable get() = when (this) {
    ComponentType.Primary -> MaterialTheme.colorScheme.primary
    ComponentType.Secondary -> MaterialTheme.colorScheme.secondary
    ComponentType.Tertiary -> MaterialTheme.colorScheme.tertiary
    ComponentType.Error -> MaterialTheme.colorScheme.error
    ComponentType.Inverse -> MaterialTheme.colorScheme.onSurface
    ComponentType.Outlined -> MaterialTheme.colorScheme.surface
    ComponentType.Neutral -> MaterialTheme.colorScheme.inverseOnSurface
}

private val ComponentType.targetContentColor @Composable get() = when (this) {
    ComponentType.Primary -> MaterialTheme.colorScheme.onPrimary
    ComponentType.Secondary -> MaterialTheme.colorScheme.onSecondary
    ComponentType.Tertiary -> MaterialTheme.colorScheme.onTertiary
    ComponentType.Error -> MaterialTheme.colorScheme.onError
    ComponentType.Inverse -> MaterialTheme.colorScheme.surface
    ComponentType.Outlined -> MaterialTheme.colorScheme.primary
    ComponentType.Neutral -> MaterialTheme.colorScheme.inverseSurface
}

private val ComponentType.disabledContainerColor @Composable get() = if (this == ComponentType.Outlined) targetContainerColor else targetContainerColor.copy(alpha = disabledAlpha)
private val ComponentType.disabledContentColor @Composable get() = targetContentColor.copy(alpha = disabledAlpha)

val ComponentType.buttonTypeColor: @Composable () -> ButtonColors
    get() = {
        when (this) {
            ComponentType.Primary -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = disabledAlpha),
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = disabledAlpha),
            )
            ComponentType.Secondary -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = disabledAlpha),
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = disabledAlpha),
            )
            ComponentType.Tertiary -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContentColor = MaterialTheme.colorScheme.tertiary.copy(alpha = disabledAlpha),
                disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = disabledAlpha),
            )
            ComponentType.Error -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = disabledAlpha),
                disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = disabledAlpha),
            )
            ComponentType.Inverse -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = disabledAlpha),
                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = disabledAlpha),
            )
            ComponentType.Outlined -> ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = disabledAlpha),
            )
            ComponentType.Neutral -> ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.inverseSurface,
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = disabledAlpha),
                disabledContainerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = disabledAlpha),
            )
        }
    }

@Composable
fun ComponentType.animatedButtonColors(
    enabled: Boolean = true,
): ButtonColors {
    // Animate container/content whenever type changes
    val containerColor by animateColorAsState(if (enabled) targetContainerColor else disabledContainerColor)
    val contentColor by animateColorAsState(if (enabled) targetContentColor else disabledContentColor)

    return ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}

val ComponentType.iconTypeColor: @Composable () -> IconButtonColors
    get() = {
        when (this) {
            ComponentType.Primary -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = disabledAlpha),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = disabledAlpha),
            )

            ComponentType.Secondary -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = disabledAlpha),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = disabledAlpha),
            )
            ComponentType.Tertiary -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = disabledAlpha),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = disabledAlpha),
            )
            ComponentType.Error -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = disabledAlpha),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                disabledContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = disabledAlpha),
            )
            ComponentType.Inverse -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = disabledAlpha),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = disabledAlpha),
            )
            ComponentType.Outlined -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = disabledAlpha),
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
            ComponentType.Neutral -> IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.inverseSurface,
                disabledContentColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = disabledAlpha),
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        }
    }

@Composable
fun ComponentType.animatedIconColors(
    enabled: Boolean = true,
): IconButtonColors {
    // Animate container/content whenever type changes
    val containerColor by animateColorAsState(if (enabled) targetContainerColor else disabledContainerColor)
    val contentColor by animateColorAsState(if (enabled) targetContentColor else disabledContentColor)

    return IconButtonDefaults.iconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}

@Composable
fun ComponentType.animatedCheckboxColors(
    enabled: Boolean = true,
): CheckboxColors {
    val containerColor by animateColorAsState(
        if (enabled) targetContainerColor else disabledContainerColor,
    )
    val contentColor by animateColorAsState(
        if (enabled) targetContentColor else disabledContentColor,
    )

    // Derive other animated color roles
    val borderColor by animateColorAsState(
        if (enabled) {
            containerColor
        } else {
            containerColor.copy(disabledAlpha)
        },
    )

    val uncheckedBoxColor = Color.Transparent

    return CheckboxColors(
        checkedCheckmarkColor = contentColor,
        uncheckedCheckmarkColor = Color.Transparent,
        checkedBoxColor = containerColor,
        uncheckedBoxColor = uncheckedBoxColor,
        disabledCheckedBoxColor = containerColor.copy(alpha = disabledAlpha),
        disabledUncheckedBoxColor = uncheckedBoxColor.copy(alpha = disabledAlpha),
        disabledIndeterminateBoxColor = containerColor.copy(alpha = disabledAlpha),
        checkedBorderColor = borderColor,
        uncheckedBorderColor = borderColor,
        disabledBorderColor = borderColor.copy(alpha = disabledAlpha),
        disabledUncheckedBorderColor = borderColor.copy(alpha = disabledAlpha),
        disabledIndeterminateBorderColor = borderColor.copy(alpha = disabledAlpha),
    )
}
