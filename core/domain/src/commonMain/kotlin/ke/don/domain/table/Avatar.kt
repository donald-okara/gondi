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


    fun url(): String {
        return when (this) {
            Aidan -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Aidan&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Adrian -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Adrian&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Alexander -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Alexander&features=birthmark,mustache&hair=long01,long02,long04,long05,long06,long07,long09,long10,long11,long12,long14,long15,long16,long17,long18,long19,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19"
            Amaya -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Amaya&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Christian -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Christian&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            George -> "https://api.dicebear.com/9.x/adventurer/svg?seed=George&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Jade -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Jade&features=birthmark,mustache&hair=long01,long02,long04,long05,long06,long07,long09,long10,long11,long12,long14,long15,long16,long17,long18,long19,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19"
            Jameson -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Jameson&features=birthmark,mustache&hair=long01,long02,long04,long05,long06,long07,long09,long10,long11,long12,long14,long15,long16,long17,long18,long19,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19"
            Jocelyn -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Jocelyn&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Katherine -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Katherine&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Leo -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Leo&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Mason -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Mason&features=birthmark,mustache&hair=long01,long02,long04,long05,long06,long07,long09,long10,long11,long12,long14,long15,long16,long17,long18,long19,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19"
            Nolan -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Nolan&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Riley -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Riley&features=birthmark,mustache&hair=long01,long02,long04,long05,long06,long07,long09,long10,long11,long12,long14,long15,long16,long17,long18,long19,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19"
            Ryker -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Ryker&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            Sawyer -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Sawyer&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
            else -> "https://api.dicebear.com/9.x/adventurer/svg?seed=Aidan&hair=long02,long03,long05,long07,long09,long10,long13,long14,long15,long21,long22,long23,long24,long25,long26,short01,short02,short03,short04,short05,short06,short07,short08,short09,short10,short11,short12,short13,short14,short15,short16,short17,short18,short19,long16&hairColor=0e0e0e,562306,796a45,85c2c6,ab2a18,b9a05f,cb6820,dba3be,e5d7a3,592454"
        }
    }

}
