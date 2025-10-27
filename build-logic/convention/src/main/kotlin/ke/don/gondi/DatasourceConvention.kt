package ke.don.gondi

import appIdentity
import ke.don.gondi.extensions.configureProjectDependencies
import ke.don.gondi.extensions.coreModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class DatasourceConvention : Plugin<Project>{
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("${appIdentity.packageName}.kotlinMultiplatformLibrary")

        configureProjectDependencies(coreModules.all)
    }
}