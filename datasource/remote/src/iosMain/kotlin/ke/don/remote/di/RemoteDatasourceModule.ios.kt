/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.di

import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.repo.AuthClient
import ke.don.local.di.localDatasourceModule
import ke.don.remote.repo.AuthClientIOS
import ke.don.remote.server.LanServer
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val serverModule: Module
    get() = module {
        includes(localDatasourceModule)
        singleOf(::LanServer).bind<LocalServer>()
    }

actual val authModule: Module = module {
    singleOf(::AuthClientIOS).bind<AuthClient>()
}
