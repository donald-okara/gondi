/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
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
