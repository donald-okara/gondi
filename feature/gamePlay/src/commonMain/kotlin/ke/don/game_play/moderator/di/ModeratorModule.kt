package ke.don.game_play.moderator.di

import ke.don.game_play.moderator.model.GondiHost
import ke.don.game_play.moderator.useCases.GameModeratorController
import ke.don.game_play.moderator.useCases.GameServerManager
import ke.don.game_play.moderator.useCases.GameSessionState
import ke.don.local.di.datastoreModule
import ke.don.local.di.sharedThemeModule
import ke.don.remote.di.remoteDatasourceModule
import ke.don.remote.di.serverModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val GAME_MODERATOR_SCOPE = "GAME_MODERATOR_SCOPE"

val moderatorModule = module {
    includes(remoteDatasourceModule, serverModule, datastoreModule, sharedThemeModule)
    scope(named(GAME_MODERATOR_SCOPE)){
        scopedOf(::GameModeratorController)
        scopedOf(::GameServerManager)
        scopedOf(::GameSessionState)
        factoryOf(::GondiHost)
    }
}