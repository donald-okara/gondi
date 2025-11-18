/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.table

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AvatarBackground {

    // GREENS
    @SerialName("green_emerald")
    GREEN_EMERALD,

    @SerialName("green_minty")
    GREEN_MINTY,

    @SerialName("green_neon")
    GREEN_NEON,

    @SerialName("green_leafy")
    GREEN_LEAFY,

    // YELLOWS
    @SerialName("yellow_bright")
    YELLOW_BRIGHT,

    @SerialName("yellow_sunny")
    YELLOW_SUNNY,

    @SerialName("yellow_banana")
    YELLOW_BANANA,

    @SerialName("yellow_golden")
    YELLOW_GOLDEN,

    // PURPLES
    @SerialName("purple_electric")
    PURPLE_ELECTRIC,

    @SerialName("purple_orchid")
    PURPLE_ORCHID,

    @SerialName("purple_lilac")
    PURPLE_LILAC,

    @SerialName("purple_amethyst")
    PURPLE_AMETHYST,

    // EXTRAS
    @SerialName("cyan_bright")
    CYAN_BRIGHT,

    @SerialName("pink_hot")
    PINK_HOT,

    @SerialName("orange_coral")
    ORANGE_CORAL,

    @SerialName("unknown")
    UNKNOWN,

    ;

    companion object {

        fun fromValue(value: String?): AvatarBackground =
            try {
                if (value == null) UNKNOWN else valueOf(value)
            } catch (e: Exception) {
                UNKNOWN
            }

        val entriesFiltered = entries.minus(UNKNOWN)
    }
}
