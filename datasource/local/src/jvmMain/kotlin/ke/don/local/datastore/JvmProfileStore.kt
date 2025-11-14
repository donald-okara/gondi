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

import ke.don.domain.table.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class JvmProfileStore : ProfileStore {
    private val prefs = Preferences.userRoot().node("app_prefs")
    private val key = PROFILE_KEY
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
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
