package ke.don.gondi

import appIdentity
import ke.don.gondi.extensions.coreModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DatasourceConvention : Plugin<Project>{
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("${appIdentity.packageName}.kotlinMultiplatformLibrary")

        dependencies {
            coreModules.all.forEach {
                add("implementation", it)
            }

        }

    }
}