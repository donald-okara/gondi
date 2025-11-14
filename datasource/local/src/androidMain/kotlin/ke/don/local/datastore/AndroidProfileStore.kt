package ke.don.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ke.don.domain.table.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class AndroidProfileStore( private val context: Context ): ProfileStore {
    private val ds = context.dataStore

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }


    val profileKey = stringPreferencesKey(PROFILE_KEY)
    override val profileFlow : Flow<Profile?> = ds.data
        .map { prefs ->
            prefs[profileKey]?.let { json.decodeFromString<Profile>(it) }
        }

    override suspend fun setProfile(profile: Profile) {
        ds.edit { prefs -> prefs[profileKey] = json.encodeToString(profile) }
    }

    override suspend fun clear() {
        ds.edit { it.clear() }
    }
}