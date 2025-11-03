package ke.don.local.di

import ke.don.local.db.LocalDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val databaseModule: Module

val localDatasourceModule = module {
    includes(databaseModule)
    singleOf(::LocalDatabase)
}