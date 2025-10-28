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

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val CardType.cardTypeColor: @Composable () -> CardColors
    get() = {
        when (this) {
            CardType.Outlined -> CardDefaults.outlinedCardColors()
            CardType.Solid -> CardDefaults.cardColors()
        }
    }

val CardBorder: BorderStroke
    @Composable
    get() = CardDefaults.outlinedCardBorder()

val DefaultCardElevation: CardElevation
    @Composable
    get() = CardDefaults.cardElevation(
        defaultElevation = 4.dp,
        pressedElevation = 8.dp,
        focusedElevation = 8.dp,
        hoveredElevation = 8.dp,
        draggedElevation = 8.dp,
        disabledElevation = 0.dp,
    )
