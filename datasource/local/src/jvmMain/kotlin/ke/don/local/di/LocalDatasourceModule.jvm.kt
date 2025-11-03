package ke.don.local.di

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