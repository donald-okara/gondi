package ke.don.remote.api

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
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
        supabase.from("profiles").upsert(profile).toDomainSingleResult<Profile>()
    }

    suspend fun logOut(){
        supabase.auth.signOut()
    }
}