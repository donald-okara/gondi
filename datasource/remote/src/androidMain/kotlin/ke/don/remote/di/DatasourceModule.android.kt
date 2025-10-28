package ke.don.remote.di

import ke.don.domain.repo.AuthClient
import ke.don.remote.repo.AuthClientAndroid
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val authModule: Module = module {
    singleOf(::AuthClientAndroid).bind<AuthClient>()
}