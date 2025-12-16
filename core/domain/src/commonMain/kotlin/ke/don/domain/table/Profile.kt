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
import ke.don.utils.analytics.PosthogCapture
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

    @OptIn(ExperimentalTime::class)
    fun captureEvent(
        event: String,
        properties: Map<String, Any>? = null,
        timestamp: Instant? = null,
    ) {
        PosthogCapture.capture(
            event = event,
            distinctId = id.ifBlank { null },

            // Event-specific context
            properties = properties,

            // Mutable user traits
            userProperties = buildMap<String, Any> {
                put("username", username)
                put("has_avatar", avatar != null)
                put("avatar_background", background.name)

                avatar?.url()?.let {
                    put("profile_url", it)
                }
            },
            // Immutable user facts
            userPropertiesSetOnce = mapOf(
                "profile_created_at" to createdAt,
            ),

            // Only if you actually use groups (safe to omit)
            groups = null,

            timestamp = timestamp,
        )
    }
}
