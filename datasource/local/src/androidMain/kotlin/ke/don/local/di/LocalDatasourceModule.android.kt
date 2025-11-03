package ke.don.local.di

import ke.don.local.db.AndroidDatabaseFactory
import ke.don.local.db.DatabaseFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val databaseModule: Module
    get() = module {
        singleOf(::AndroidDatabaseFactory).bind<DatabaseFactory>()
    }