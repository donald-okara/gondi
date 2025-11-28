/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.player.di

import ke.don.game_play.player.model.GondiClient
import ke.don.game_play.player.useCases.GameClientManager
import ke.don.game_play.player.useCases.GameClientState
import ke.don.game_play.player.useCases.GamePlayerController
import ke.don.local.di.datastoreModule
import ke.don.remote.di.gameplayDatasourceModule
import ke.don.remote.di.remoteDatasourceModule
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val GAME_PLAYER_SCOPE = "GAME_PLAYER_SCOPE"

val playerModule = module {
    includes(remoteDatasourceModule, gameplayDatasourceModule, datastoreModule)

    scope(named(GAME_PLAYER_SCOPE)) {
        scopedOf(::GamePlayerController)
        scopedOf(::GameClientState)
        scopedOf(::GameClientManager)
    }

    factory { GondiClient(getKoin()) }
}
