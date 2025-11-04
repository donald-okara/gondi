package ke.don.remote.di

import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.repo.AuthClient
import ke.don.remote.repo.AuthClientIOS
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val serverModule: Module
    get() = module {
        singleOf(::LanServer).bind<LocalServer>()
    }

actual val authModule: Module = module {
    singleOf(::AuthClientIOS).bind<AuthClient>()
}
