package ke.don.profile.model

import ke.don.domain.table.Profile

data class EditProfileState(
    val editProfile: Profile = Profile(),
    val fetchedProfile: Profile = Profile()
)
