/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi

import ke.don.authentication.di.authModule
import ke.don.game_play.moderator.di.moderatorModule
import ke.don.home.di.homeModule
import ke.don.local.di.localDatasourceModule
import ke.don.local.di.sharedThemeModule
import ke.don.profile.di.profileModule
import ke.don.remote.di.gameplayDatasourceModule
import ke.don.remote.di.remoteDatasourceModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            gameplayDatasourceModule,
            remoteDatasourceModule,
            localDatasourceModule,
            sharedThemeModule,

            authModule,
            profileModule,
            homeModule,
            moderatorModule
        )
    }
}
