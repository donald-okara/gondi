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

import ke.don.local.datastore.IosProfileStore
import ke.don.local.datastore.IosThemeStore
import ke.don.local.datastore.ProfileStore
import ke.don.local.datastore.ThemeStore
import ke.don.local.db.DatabaseFactory
import ke.don.local.db.IOSDatabaseFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val databaseModule: Module
    get() = module {
        singleOf(::IOSDatabaseFactory).bind<DatabaseFactory>()
    }
actual val datastoreModule: Module
    get() = module {
        singleOf(::IosProfileStore).bind<ProfileStore>()
        singleOf(::IosThemeStore).bind<ThemeStore>()
    }