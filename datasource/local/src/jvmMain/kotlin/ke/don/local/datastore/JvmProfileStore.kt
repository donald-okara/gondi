package ke.don.local.datastore

import ke.don.domain.datastore.Theme
import ke.don.domain.table.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class JvmProfileStore: ProfileStore {
    private val prefs = Preferences.userRoot().node("app_prefs")
    private val key = PROFILE_KEY
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    private val _profileFlow = MutableStateFlow(loadProfile())

    override val profileFlow: Flow<Profile?> = _profileFlow.asStateFlow()

    private fun loadProfile(): Profile? {
        val stored = prefs.get(key, null) ?: return null
        return json.decodeFromString(stored)
    }

    override suspend fun setProfile(profile: Profile) {
        prefs.put(key, json.encodeToString(profile))
        _profileFlow.value = profile
    }

    override suspend fun clear() {
        prefs.remove(key)
        _profileFlow.value = null
    }
}