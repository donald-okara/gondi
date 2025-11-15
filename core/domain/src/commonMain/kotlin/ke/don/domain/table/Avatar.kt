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

import ke.don.domain.table.AvatarBackground.UNKNOWN
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Avatar {
    @SerialName("aidan") Aidan,
    @SerialName("adrian") Adrian,
    @SerialName("amaya") Amaya,
    @SerialName("alexander") Alexander,
    @SerialName("christian") Christian,
    @SerialName("george") George,
    @SerialName("jade") Jade,
    @SerialName("jameson") Jameson,
    @SerialName("jocelyn") Jocelyn,
    @SerialName("katherine") Katherine,
    @SerialName("leo") Leo,
    @SerialName("mason") Mason,
    @SerialName("nolan") Nolan,
    @SerialName("riley") Riley,
    @SerialName("ryker") Ryker,
    @SerialName("sawyer") Sawyer,

    @SerialName("unknown") Unknown;

    companion object {

        // mapping of serialName → enum
        private val map = mapOf(
            "aidan" to Aidan,
            "adrian" to Adrian,
            "amaya" to Amaya,
            "alexander" to Alexander,
            "christian" to Christian,
            "george" to George,
            "jade" to Jade,
            "jameson" to Jameson,
            "jocelyn" to Jocelyn,
            "katherine" to Katherine,
            "leo" to Leo,
            "mason" to Mason,
            "nolan" to Nolan,
            "riley" to Riley,
            "ryker" to Ryker,
            "sawyer" to Sawyer,
            "unknown" to Unknown,
        )

        fun fromValue(value: String?): Avatar =
            if (value == null) Unknown else map[value] ?: Unknown

        val entriesFiltered = entries.minus(Unknown)
    }
}
