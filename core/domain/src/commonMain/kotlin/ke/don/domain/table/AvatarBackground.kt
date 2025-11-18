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

        private val map = mapOf(
            "green_emerald" to GREEN_EMERALD,
            "green_minty" to GREEN_MINTY,
            "green_neon" to GREEN_NEON,
            "green_leafy" to GREEN_LEAFY,

            "yellow_bright" to YELLOW_BRIGHT,
            "yellow_sunny" to YELLOW_SUNNY,
            "yellow_banana" to YELLOW_BANANA,
            "yellow_golden" to YELLOW_GOLDEN,

            "purple_electric" to PURPLE_ELECTRIC,
            "purple_orchid" to PURPLE_ORCHID,
            "purple_lilac" to PURPLE_LILAC,
            "purple_amethyst" to PURPLE_AMETHYST,

            "cyan_bright" to CYAN_BRIGHT,
            "pink_hot" to PINK_HOT,
            "orange_coral" to ORANGE_CORAL,

            "unknown" to UNKNOWN,
        )

        fun fromValue(value: String?): AvatarBackground =
            if (value == null) UNKNOWN else map[value] ?: UNKNOWN

        val entriesFiltered = entries.minus(UNKNOWN)
    }
}
