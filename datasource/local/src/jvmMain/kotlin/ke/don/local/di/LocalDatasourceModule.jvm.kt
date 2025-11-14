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

import ke.don.local.JvmThemeStore
import ke.don.local.datastore.ThemeStore
import ke.don.local.db.DatabaseFactory
import ke.don.local.db.JVMDatabaseFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val databaseModule: Module
    get() = module {
        singleOf(::JVMDatabaseFactory).bind<DatabaseFactory>()
    }
actual val platformThemeModule: Module
    get() = module {
        singleOf(::JvmThemeStore).bind<ThemeStore>()
    }