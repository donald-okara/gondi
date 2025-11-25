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
enum class Avatar {
    @SerialName("aidan")
    Aidan,

    @SerialName("adrian")
    Adrian,

    @SerialName("amaya")
    Amaya,

    @SerialName("alexander")
    Alexander,

    @SerialName("avery")
    Avery,

    @SerialName("christian")
    Christian,

    @SerialName("george")
    George,

    @SerialName("jade")
    Jade,

    @SerialName("jameson")
    Jameson,

    @SerialName("jocelyn")
    Jocelyn,

    @SerialName("katherine")
    Katherine,

    @SerialName("kimberly")
    Kimberly,

    @SerialName("leo")
    Leo,

    @SerialName("maria")
    Maria,

    @SerialName("mason")
    Mason,

    @SerialName("nolan")
    Nolan,

    @SerialName("riley")
    Riley,

    @SerialName("ryker")
    Ryker,

    @SerialName("sarah")
    Sarah,

    @SerialName("sawyer")
    Sawyer,

    @SerialName("unknown")
    Unknown,

    ;

    companion object {

        fun fromValue(value: String?): Avatar =
            try {
                if (value == null) Unknown else valueOf(value)
            } catch (e: Exception) {
                Unknown
            }

        val entriesFiltered = entries.minus(Unknown)
    }
}
