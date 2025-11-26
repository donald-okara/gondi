package ke.don.game_play.player.di

import ke.don.game_play.player.model.GondiClient
import ke.don.game_play.player.useCases.GameClientManager
import ke.don.game_play.player.useCases.GameClientState
import ke.don.game_play.player.useCases.GamePlayerController
import ke.don.local.di.datastoreModule
import ke.don.remote.di.gameplayDatasourceModule
import ke.don.remote.di.remoteDatasourceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val GAME_PLAYER_SCOPE = "GAME_MODERATOR_SCOPE"


val playerModule = module {
    includes(remoteDatasourceModule, gameplayDatasourceModule, datastoreModule)

    scope(named(GAME_PLAYER_SCOPE)) {
        scopedOf(::GamePlayerController)
        scopedOf(::GameClientState)
        scopedOf(::GameClientManager)
    }

    factory { GondiClient(getKoin()) }

}