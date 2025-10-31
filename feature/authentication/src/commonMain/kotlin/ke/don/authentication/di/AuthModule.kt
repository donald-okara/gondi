package ke.don.authentication.di

import ke.don.authentication.model.AuthModel
import ke.don.remote.di.datasourceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authModule = module {
    includes(datasourceModule)
    factoryOf(::AuthModel)
}