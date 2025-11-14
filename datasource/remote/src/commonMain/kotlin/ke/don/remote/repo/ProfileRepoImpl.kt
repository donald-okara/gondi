package ke.don.remote.repo

import ke.don.domain.repo.ProfileRepository
import ke.don.domain.table.Profile
import ke.don.local.datastore.ProfileStore
import ke.don.remote.api.ApiClient
import ke.don.utils.Logger
import ke.don.utils.result.NetworkError
import ke.don.utils.result.Result
import kotlin.math.log


class ProfileRepoImpl(
    private val apiClient: ApiClient,
    private val profileStore: ProfileStore
): ProfileRepository {
    private val logger = Logger("ProfileRepo")
    override suspend fun getProfile(): Result<Profile, NetworkError> = apiClient.getProfile().also {
        if (it is Result.Success) {
            profileStore.setProfile(it.data)
        }
        logger.info(it.toString())
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile, NetworkError> = apiClient.updateProfile(profile).also {
        if (it is Result.Success) {
            profileStore.setProfile(it.data)
        }
    }

    override suspend fun logOut() = apiClient.logOut().also {
        profileStore.clear()
    }
}