package ke.don.profile.model

import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

sealed interface EditProfileEvent {
    data class OnUsernameChanged(val username: String) : EditProfileEvent

    data class OnAvatarChanged(val avatar: Avatar) : EditProfileEvent

    data class OnBackgroundChanged(val background: AvatarBackground) : EditProfileEvent

    data class OnSaveProfile(val onSaved: () -> Unit) : EditProfileEvent
}