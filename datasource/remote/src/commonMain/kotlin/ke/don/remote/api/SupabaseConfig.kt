package ke.don.remote.api

import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.plugins.HttpTimeout

object SupabaseConfig {
    @OptIn(SupabaseInternal::class)
    val supabase = createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_KEY,
    ) {
        install(Postgrest)
        installPlatformAuth()
        httpConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 15_000
            }
        }
    }
}


expect fun SupabaseClientBuilder.installPlatformAuth()