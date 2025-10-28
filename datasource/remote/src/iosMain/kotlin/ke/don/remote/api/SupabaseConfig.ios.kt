package ke.don.remote.api

import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.auth.Auth

actual fun SupabaseClientBuilder.installPlatformAuth() {
    install(Auth) {
        host = "auth"
        scheme = "gondi"
    }
}