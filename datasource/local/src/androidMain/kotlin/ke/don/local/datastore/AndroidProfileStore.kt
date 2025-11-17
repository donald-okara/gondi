/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ke.don.domain.table.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "profile_prefs")

class AndroidProfileStore(private val context: Context) : ProfileStore {
    private val ds = context.dataStore

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val profileKey = stringPreferencesKey(PROFILE_KEY)
    override val profileFlow: Flow<Profile?> = ds.data
        .map { prefs ->
            prefs[profileKey]?.let { json.decodeFromString<Profile>(it) }
        }

    override suspend fun setProfile(profile: Profile) {
        ds.edit { prefs -> prefs[profileKey] = json.encodeToString(profile) }
    }

    override suspend fun clear() {
        ds.edit { it.remove(profileKey) }
    }
}
