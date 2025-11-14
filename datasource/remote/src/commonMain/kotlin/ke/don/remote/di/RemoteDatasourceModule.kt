/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.di

import ke.don.domain.gameplay.GameEngine
import ke.don.domain.gameplay.ModeratorEngine
import ke.don.domain.repo.ProfileRepository
import ke.don.local.di.datastoreModule
import ke.don.remote.api.ApiClient
import ke.don.remote.repo.ProfileRepoImpl
import ke.don.remote.server.DefaultGameEngine
import ke.don.remote.server.DefaultModeratorEngine
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val authDatasourceModule: Module
expect val serverModule: Module

val gameplayDatasourceModule = module {
    includes(serverModule)
    singleOf(::DefaultGameEngine).bind<GameEngine>()
    singleOf(::DefaultModeratorEngine).bind<ModeratorEngine>()
}

val remoteDatasourceModule = module {
    includes(datastoreModule)
    singleOf(::ApiClient)
    singleOf(::ProfileRepoImpl).bind<ProfileRepository>()
}