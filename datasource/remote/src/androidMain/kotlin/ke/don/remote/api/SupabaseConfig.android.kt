package ke.don.remote.api

import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.FlowType

actual fun SupabaseClientBuilder.installPlatformAuth() {
    install(Auth) {
        flowType = FlowType.PKCE
        host = "auth"
        scheme = "gondi"
        defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
    }
}
