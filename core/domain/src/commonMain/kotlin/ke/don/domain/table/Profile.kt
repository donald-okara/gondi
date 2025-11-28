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

import ke.don.domain.state.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String = "",
    val username: String = "",
    val avatar: Avatar? = null,
    @SerialName("avatar_background") val background: AvatarBackground = AvatarBackground.entries.first(),
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
) {
    fun toPlayer(): Player {
        return Player(
            id = id,
            name = username,
            avatar = avatar,
            background = background,
        )
    }
}
