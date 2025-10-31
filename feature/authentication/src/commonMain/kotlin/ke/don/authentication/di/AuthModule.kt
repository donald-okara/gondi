package ke.don.authentication.di

import ke.don.remote.di.datasourceModule
import org.koin.dsl.module

val authModule = module {
    includes(datasourceModule)
}