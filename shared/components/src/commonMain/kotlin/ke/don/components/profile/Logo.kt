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

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import ke.don.resources.Resources
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdaptiveLogo(
    modifier: Modifier = Modifier,
) {
    AdaptiveIcon(
        modifier = modifier,
        painterResource = Resources.Images.LOGO,
        contentDescription = "App Logo",
    )
}

@Composable
fun AdaptiveIcon(
    modifier: Modifier = Modifier,
    painterResource: DrawableResource,
    contentDescription: String?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    logoColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Image(
        painter = painterResource(painterResource),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(logoColor),
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
    )
}
