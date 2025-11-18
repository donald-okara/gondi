/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.profile.model

import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

sealed interface EditProfileEvent {
    data class OnUsernameChanged(val username: String) : EditProfileEvent

    data class OnAvatarChanged(val avatar: Avatar) : EditProfileEvent

    data class OnBackgroundChanged(val background: AvatarBackground) : EditProfileEvent

    data class OnSaveProfile(val onSaved: () -> Unit) : EditProfileEvent
}
