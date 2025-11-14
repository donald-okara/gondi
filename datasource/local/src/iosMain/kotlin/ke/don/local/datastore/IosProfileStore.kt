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
import platform.Foundation.NSUserDefaults

class IosProfileStore : ProfileStore {
    private val defaults = NSUserDefaults.standardUserDefaults()
    private val profileKey = PROFILE_KEY
    private val _profileFlow = MutableStateFlow(loadProfile())

    override val profileFlow: Flow<Profile?> = _profileFlow.asStateFlow()

    private fun loadProfile(): Profile? {
        return null
    }

    override suspend fun setProfile(profile: Profile) {
        defaults.setObject(profile.username, forKey = profileKey)
        _profileFlow.value = profile
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(profileKey)
        _profileFlow.value = null
    }
}
