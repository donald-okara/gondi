package ke.don.profile.di

import ke.don.profile.model.EditProfileModel
import ke.don.remote.di.remoteDatasourceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileModule = module {
    includes(remoteDatasourceModule)
    factoryOf(::EditProfileModel)
}