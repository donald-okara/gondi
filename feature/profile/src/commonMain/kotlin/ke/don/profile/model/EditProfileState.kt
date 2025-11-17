package ke.don.profile.model

import ke.don.domain.table.Profile
import ke.don.utils.result.ResultStatus

data class EditProfileState(
    val editProfile: Profile = Profile(),
    val fetchedProfile: Profile = Profile(),

    val saveStatus: ResultStatus<Profile> = ResultStatus.Idle
)
