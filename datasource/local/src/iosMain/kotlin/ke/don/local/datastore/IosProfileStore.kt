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
import ke.don.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosProfileStore : ProfileStore {

    private val defaults = NSUserDefaults.standardUserDefaults()
    private val profileKey = PROFILE_KEY
    private val json = Json { ignoreUnknownKeys = true }

    private val _profileFlow = MutableStateFlow(loadProfile())
    override val profileFlow: Flow<Profile?> = _profileFlow.asStateFlow()

    private val logger = Logger("IosProfileStore")

    private fun loadProfile(): Profile? {
        val stored = defaults.stringForKey(profileKey) ?: return null

        return try {
            json.decodeFromString<Profile>(stored)
        } catch (e: Exception) {
            logger.error("Error loading profile: ${e.message}", e)
            null
        }
    }

    override suspend fun setProfile(profile: Profile) {
        val encoded = json.encodeToString(profile)
        defaults.setObject(encoded, forKey = profileKey)
        _profileFlow.value = profile
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(profileKey)
        _profileFlow.value = null
    }
}
