package ke.don.gondi

import appIdentity
import ke.don.gondi.extensions.coreModules
import ke.don.gondi.extensions.datasourceModules
import ke.don.gondi.extensions.sharedModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureConvention : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            listOf(
                "kotlinMultiplatformLibrary",
                "composeMultiplatformPlugin"
            ).forEach { id ->
                apply("${appIdentity.packageName}.$id")
            }
        }

        dependencies {
            coreModules.all.forEach {
                add("implementation", it)
            }
            datasourceModules.all.forEach {
                add("implementation", it)
            }
            sharedModules.all.forEach {
                add("implementation", it)
            }
        }

    }
}
