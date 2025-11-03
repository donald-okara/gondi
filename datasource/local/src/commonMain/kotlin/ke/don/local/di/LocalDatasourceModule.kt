package ke.don.local.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val localDatasourceModule = module {
    includes(databaseModule)
}