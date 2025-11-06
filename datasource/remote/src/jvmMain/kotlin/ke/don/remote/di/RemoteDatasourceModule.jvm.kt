package ke.don.remote.di

import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.repo.AuthClient
import ke.don.local.di.localDatasourceModule
import ke.don.remote.repo.AuthClientJvm
import ke.don.remote.server.LanAdvertiserJvm
import ke.don.remote.server.LanDiscoveryJvm
import ke.don.remote.server.LanServerJvm
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val serverModule: Module
    get() = module {
        includes(localDatasourceModule)
        singleOf(::LanServerJvm).bind<LocalServer>()
        singleOf(::LanAdvertiserJvm).bind<LanAdvertiser>()
        singleOf(::LanDiscoveryJvm).bind<LanDiscovery>()
    }

actual val authModule: Module = module {
    singleOf(::AuthClientJvm).bind<AuthClient>()
}
