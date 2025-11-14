/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.di

import ke.don.local.datastore.ThemeRepository
import ke.don.local.db.LocalDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val databaseModule: Module

expect val platformThemeModule: Module

val sharedThemeModule = module {
    includes(platformThemeModule)
    singleOf( ::ThemeRepository)
}

val localDatasourceModule = module {
    includes(databaseModule)
    singleOf(::LocalDatabase)
}
