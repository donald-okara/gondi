package ke.don.game_play.di

import ke.don.game_play.model.GondiClient
import ke.don.game_play.model.GondiHost
import ke.don.local.di.datastoreModule
import ke.don.local.di.sharedThemeModule
import ke.don.remote.di.remoteDatasourceModule
import ke.don.remote.di.serverModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val gamePlayModule = module {
    includes(remoteDatasourceModule, serverModule, datastoreModule, sharedThemeModule)
    factoryOf(::GondiClient)
    factoryOf(::GondiHost)
}