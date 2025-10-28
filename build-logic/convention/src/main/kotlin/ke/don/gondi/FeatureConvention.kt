package ke.don.gondi

import appIdentity
import ke.don.gondi.extensions.configureKoin
import ke.don.gondi.extensions.configureProjectDependencies
import ke.don.gondi.extensions.configureVoyager
import ke.don.gondi.extensions.coreModules
import ke.don.gondi.extensions.datasourceModules
import ke.don.gondi.extensions.sharedModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

        configureProjectDependencies(coreModules.all, datasourceModules.all, sharedModules.all)
        configureKoin()
        configureVoyager()
    }
}
