/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * Render a Material3 Card styled as a token with configurable appearance and optional click handling.
 *
 * The card uses provided shape, colors, elevation and border. If `onClick` is `null` the card is non-clickable;
 * if `onClick` is provided the card becomes clickable and invokes the handler when tapped. By default `colors`
 * and `border` are derived from `cardType`.
 *
 * @param onClick Optional click callback; when non-null enables click interaction for the card.
 * @param shape Card geometry used to clip and draw the card's corners.
 * @param cardType Style selector that determines default colors and whether a border is applied.
 * @param colors Card color scheme; defaults to a value derived from `cardType`.
 * @param elevation Visual elevation for the card's shadow.
 * @param border Optional border stroke; by default applied only when `cardType` is `CardType.Outlined`.
 * @param content Composable content placed inside the card's ColumnScope.
 */
@Composable
fun CardToken(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    cardType: CardType = CardType.Outlined,
    colors: CardColors = cardType.cardTypeColor(),
    elevation: CardElevation = DefaultCardElevation,
    border: BorderStroke? = if (cardType == CardType.Outlined) CardBorder else null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick == null) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            content = content,
        )
    } else {
        Card(
            modifier = modifier,
            onClick = onClick,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            content = content,
        )
    }
}