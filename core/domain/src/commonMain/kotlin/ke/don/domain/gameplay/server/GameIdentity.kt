/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.gameplay.server

import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

data class GameIdentity(
    val id: String,
    val serviceHost: String = "",
    val serviceType: String = SERVICE_TYPE,
    val servicePort: Int = 8080,
    val gameName: String,
    val moderatorName: String,
    val moderatorAvatar: Avatar? = null,
    val moderatorAvatarBackground: AvatarBackground,
)
