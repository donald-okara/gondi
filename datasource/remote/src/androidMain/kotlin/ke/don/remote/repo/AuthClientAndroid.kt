package ke.don.remote.repo

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import ke.don.domain.repo.AuthClient
import ke.don.remote.api.SupabaseConfig.supabase

class AuthClientAndroid: AuthClient {
    override suspend fun signInWithGoogle() = supabase.auth.signInWith(Google)

}