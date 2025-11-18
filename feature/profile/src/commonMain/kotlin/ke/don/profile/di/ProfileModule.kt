/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.profile.di

import ke.don.profile.model.EditProfileModel
import ke.don.remote.di.remoteDatasourceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileModule = module {
    includes(remoteDatasourceModule)
    factoryOf(::EditProfileModel)
}
