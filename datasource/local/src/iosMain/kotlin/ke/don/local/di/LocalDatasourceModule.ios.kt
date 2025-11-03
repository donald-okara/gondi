package ke.don.local.di

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