/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.state

import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val role: Role? = null,
    val avatar: Avatar? = null,
    val background: AvatarBackground = AvatarBackground.entries.first(),
    @SerialName("is_alive") val isAlive: Boolean = true,
    @SerialName("last_action") val lastAction: PlayerAction? = null,
    @SerialName("known_identities") val knownIdentities: List<KnownIdentity> = emptyList(),
)

@Serializable
data class KnownIdentity(
    val playerId: String,
    val role: Role
)
