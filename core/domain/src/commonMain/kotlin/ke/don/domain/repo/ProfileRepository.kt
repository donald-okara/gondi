package ke.don.domain.repo

import ke.don.domain.table.Profile
import ke.don.utils.result.NetworkError
import ke.don.utils.result.Result

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile, NetworkError>
    suspend fun updateProfile(profile: Profile): Result<Profile, NetworkError>

    suspend fun logOut()
}
