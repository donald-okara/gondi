/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.game_play.moderator.di

import ke.don.game_play.moderator.model.GondiHost
import ke.don.game_play.moderator.useCases.GameModeratorController
import ke.don.game_play.moderator.useCases.GameServerManager
import ke.don.game_play.moderator.useCases.GameSessionState
import ke.don.local.di.datastoreModule
import ke.don.local.di.sharedThemeModule
import ke.don.remote.di.gameplayDatasourceModule
import ke.don.remote.di.remoteDatasourceModule
import ke.don.remote.di.serverModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val GAME_MODERATOR_SCOPE = "GAME_MODERATOR_SCOPE"

val moderatorModule = module {
    includes(remoteDatasourceModule, gameplayDatasourceModule, datastoreModule)
    scope(named(GAME_MODERATOR_SCOPE)) {
        scopedOf(::GameModeratorController)
        scopedOf(::GameServerManager)
        scopedOf(::GameSessionState)
        factoryOf(::GondiHost)
    }
}
