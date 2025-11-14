package ke.don.local.datastore

import ke.don.domain.table.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileStore {
    val profileFlow: Flow<Profile?>
    suspend fun setProfile(profile: Profile)
    suspend fun clear()
}

const val PROFILE_KEY = "profile"
