/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.design_system.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.design_system.components.button.ButtonToken
import ke.don.design_system.components.button.ComponentType
import ke.don.design_system.components.button.iconTypeColor
import ke.don.design_system.components.icon.IconToken
import ke.don.koffee.domain.style
import ke.don.koffee.helpers.COMPACT_BREAK_POINT
import ke.don.koffee.helpers.MAX_NON_COMPACT_WIDTH
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType

@Composable
fun Toast(
    modifier: Modifier = Modifier,
    data: ToastData,
) {
    BoxWithConstraints(modifier = modifier) { // no fill here; respect parent alignment
        val isCompact = maxWidth < COMPACT_BREAK_POINT

        val sizeMod = sizeMod(isCompact)
        val icon = data.type.style.icon

        CardToken(
            modifier = sizeMod,
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            ToastComponent(
                data = data,
                icon = icon,
            )
        }
    }
}

@Composable
fun ToastComponent(
    modifier: Modifier = Modifier,
    data: ToastData,
    icon: ImageVector,
) {
    val typeMap = when (data.type) {
        ToastType.Success -> ComponentType.Secondary
        ToastType.Error -> ComponentType.Error
        ToastType.Info -> ComponentType.Primary
        ToastType.Warning -> ComponentType.Tertiary
        ToastType.Neutral -> ComponentType.Inverse
    }

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconToken(
                imageVector = icon,
                contentDescription = null,
                buttonType = typeMap,
                colors = typeMap.iconTypeColor(),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = data.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (data.description.isNotEmpty()) {
                    Text(
                        text = data.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }

        if (data.primaryAction != null || data.secondaryAction != null) {
            ToastActionRow(
                secondaryAction = data.secondaryAction,
                primaryAction = data.primaryAction,
                type = typeMap,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Composable
fun ToastActionRow(
    modifier: Modifier = Modifier,
    secondaryAction: ToastAction?,
    primaryAction: ToastAction?,
    type: ComponentType,
) {
    val buttonHeight = 32.dp
    val buttonTextStyle = MaterialTheme.typography.labelSmall

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        secondaryAction?.let { action ->
            ButtonToken(
                modifier = Modifier.defaultMinSize(minHeight = buttonHeight),
                buttonType = ComponentType.Neutral,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                onClick = action.onClick,
            ) {
                Text(action.label, style = buttonTextStyle)
            }
        }

        primaryAction?.let { action ->
            ButtonToken(
                modifier = Modifier.defaultMinSize(minHeight = buttonHeight),
                buttonType = type,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                onClick = action.onClick,
            ) {
                Text(action.label, style = buttonTextStyle)
            }
        }
    }
}

fun sizeMod(isCompact: Boolean): Modifier {
    return if (isCompact) {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    } else {
        Modifier
            .wrapContentWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .widthIn(max = MAX_NON_COMPACT_WIDTH) // cap on larger screens
    }
}
