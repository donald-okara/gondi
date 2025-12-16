/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import gondi.shared.resources.generated.resources.Res
import gondi.shared.resources.generated.resources.adrian
import gondi.shared.resources.generated.resources.aidan
import gondi.shared.resources.generated.resources.alexander
import gondi.shared.resources.generated.resources.amaya
import gondi.shared.resources.generated.resources.avery
import gondi.shared.resources.generated.resources.christian
import gondi.shared.resources.generated.resources.george
import gondi.shared.resources.generated.resources.jade
import gondi.shared.resources.generated.resources.jameson
import gondi.shared.resources.generated.resources.jocelyn
import gondi.shared.resources.generated.resources.katherine
import gondi.shared.resources.generated.resources.kimberly
import gondi.shared.resources.generated.resources.leo
import gondi.shared.resources.generated.resources.maria
import gondi.shared.resources.generated.resources.mason
import gondi.shared.resources.generated.resources.nolan
import gondi.shared.resources.generated.resources.riley
import gondi.shared.resources.generated.resources.ryker
import gondi.shared.resources.generated.resources.sarah
import gondi.shared.resources.generated.resources.sawyer
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import org.jetbrains.compose.resources.DrawableResource

fun AvatarBackground.color(): Color {
    return when (this) {
        // GREENS
        AvatarBackground.GREEN_EMERALD -> Color(0xFF00E676)
        AvatarBackground.GREEN_MINTY -> Color(0xFF1DE9B6)
        AvatarBackground.GREEN_NEON -> Color(0xFF69F0AE)
        AvatarBackground.GREEN_LEAFY -> Color(0xFF00C853)

        // YELLOWS
        AvatarBackground.YELLOW_BRIGHT -> Color(0xFFFFEA00)
        AvatarBackground.YELLOW_SUNNY -> Color(0xFFFFEB3B)
        AvatarBackground.YELLOW_BANANA -> Color(0xFFFFF176)
        AvatarBackground.YELLOW_GOLDEN -> Color(0xFFFFD600)

        // PURPLES
        AvatarBackground.PURPLE_ELECTRIC -> Color(0xFFD500F9)
        AvatarBackground.PURPLE_ORCHID -> Color(0xFFE040FB)
        AvatarBackground.PURPLE_LILAC -> Color(0xFFB388FF)
        AvatarBackground.PURPLE_AMETHYST -> Color(0xFF7C4DFF)

        // EXTRAS
        AvatarBackground.CYAN_BRIGHT -> Color(0xFF18FFFF)
        AvatarBackground.PINK_HOT -> Color(0xFFFF4081)
        AvatarBackground.ORANGE_CORAL -> Color(0xFFFF6E40)
        else -> Color(0xFF00E676)
    }
}

/**
 * Credits for the assets https://www.dicebear.com/playground/, avatar style: Adventurer
 */

fun Avatar.painter(): DrawableResource {
    val drawableRoute = Res.drawable
    return when (this) {
        Avatar.Adrian -> drawableRoute.adrian
        Avatar.Aidan -> drawableRoute.aidan
        Avatar.Alexander -> drawableRoute.alexander
        Avatar.Amaya -> drawableRoute.amaya
        Avatar.Avery -> drawableRoute.avery
        Avatar.Christian -> drawableRoute.christian
        Avatar.George -> drawableRoute.george
        Avatar.Jade -> drawableRoute.jade
        Avatar.Jameson -> drawableRoute.jameson
        Avatar.Jocelyn -> drawableRoute.jocelyn
        Avatar.Katherine -> drawableRoute.katherine
        Avatar.Kimberly -> drawableRoute.kimberly
        Avatar.Leo -> drawableRoute.leo
        Avatar.Maria -> drawableRoute.maria
        Avatar.Mason -> drawableRoute.mason
        Avatar.Nolan -> drawableRoute.nolan
        Avatar.Riley -> drawableRoute.riley
        Avatar.Ryker -> drawableRoute.ryker
        Avatar.Sarah -> drawableRoute.sarah
        Avatar.Sawyer -> drawableRoute.sawyer
        else -> drawableRoute.adrian
    }
}

fun Color.onColor(): Color {
    return if (this.luminance() > 0.5f) Color.Black else Color.White
}

fun Color.onColorWithOverlay(): Color {
    return if (this.luminance() > 0.5f) {
        Color.Black.copy(alpha = 0.75f)
    } else {
        Color.White.copy(alpha = 0.85f)
    }
}
