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

interface ProfileStore {
    val profileFlow: Flow<Profile?>
    suspend fun setProfile(profile: Profile)
    suspend fun clear()
}

const val PROFILE_KEY = "profile"
