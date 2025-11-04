package ke.don.remote.di

import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.repo.AuthClient
import ke.don.remote.repo.AuthClientAndroid
import ke.don.remote.server.LanAdvertiserAndroid
import ke.don.remote.server.LanDiscoveryAndroid
import ke.don.remote.server.LanServerAndroid
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val serverModule: Module
    get() = module {
        singleOf(::LanServerAndroid).bind<LocalServer>()
        singleOf(::LanDiscoveryAndroid).bind<LanDiscovery>()
        singleOf(::LanAdvertiserAndroid).bind<LanAdvertiser>()
    }

actual val authModule: Module = module {
    singleOf(::AuthClientAndroid).bind<AuthClient>()
}
