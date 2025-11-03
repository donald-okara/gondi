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
import gondi.shared.resources.generated.resources.amaya
import gondi.shared.resources.generated.resources.christian
import gondi.shared.resources.generated.resources.george
import gondi.shared.resources.generated.resources.jocelyn
import gondi.shared.resources.generated.resources.katherine
import gondi.shared.resources.generated.resources.leo
import gondi.shared.resources.generated.resources.nolan
import gondi.shared.resources.generated.resources.ryker
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
    }
}

fun Avatar.painter(): DrawableResource {
    val drawableRoute = Res.drawable
    return when (this) {
        Avatar.Aidan -> drawableRoute.aidan
        Avatar.Adrian -> drawableRoute.adrian
        Avatar.Amaya -> drawableRoute.amaya
        Avatar.Christian -> drawableRoute.christian
        Avatar.George -> drawableRoute.george
        Avatar.Jocelyn -> drawableRoute.jocelyn
        Avatar.Katherine -> drawableRoute.katherine
        Avatar.Leo -> drawableRoute.leo
        Avatar.Nolan -> drawableRoute.nolan
        Avatar.Ryker -> drawableRoute.ryker
        Avatar.Sawyer -> drawableRoute.sawyer
    }
}

fun Avatar.url(): String {
    return when (this) {
        Avatar.Aidan -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Aidan&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Adrian -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Adrian&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Amaya -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Amaya&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Christian -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Christian&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.George -> "https://api.dicebear.com/9.x/adventurer/svg?seed=George&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Jocelyn -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Jocelyn&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Katherine -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Katherine&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Leo -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Leo&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Nolan -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Nolan&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Ryker -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Ryker&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        Avatar.Sawyer -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Sawyer&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
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
