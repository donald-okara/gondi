package ke.don.home.di

import ke.don.home.model.HomeModel
import ke.don.local.di.datastoreModule
import ke.don.local.di.sharedThemeModule
import ke.don.remote.di.remoteDatasourceModule
import ke.don.remote.di.serverModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeModule = module {
    includes(remoteDatasourceModule, serverModule, datastoreModule, sharedThemeModule)
    singleOf(::HomeModel)
}