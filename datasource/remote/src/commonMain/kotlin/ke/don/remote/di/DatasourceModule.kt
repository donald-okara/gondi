package ke.don.remote.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val authModule: Module

val datasourceModule = module {
    includes(authModule)
}