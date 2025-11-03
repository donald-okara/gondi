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

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String = "",
    val avatar: Avatar? = null,
    val email: String = "",
    val background: AvatarBackground = AvatarBackground.entries.first(),
)
