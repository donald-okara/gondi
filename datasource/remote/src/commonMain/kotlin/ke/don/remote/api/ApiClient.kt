/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.api

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import ke.don.domain.table.Profile
import ke.don.remote.api.SupabaseConfig.supabase
import ke.don.utils.result.NetworkError
import ke.don.utils.result.Result

class ApiClient {
    suspend fun getProfile(): Result<Profile, NetworkError> = runCatchingNetwork {
        val userId = supabase.auth.currentUserOrNull()?.id

        supabase.from("profiles").select {
            filter { Profile::id eq userId }
        }.toDomainSingleResult<Profile>()
    }

    suspend fun updateProfile(profile: Profile): Result<Profile, NetworkError> = runCatchingNetwork {
        supabase.from("profiles").upsert(profile) {
            select()
        }.toDomainSingleResult<Profile>()
    }

    suspend fun logOut() {
        supabase.auth.signOut()
    }
}
