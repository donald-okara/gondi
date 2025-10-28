package ke.don.gondi

import ke.don.gondi.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
    /**
     * Configures the Gradle project for Compose Multiplatform by applying required Compose-related plugins
     * and adding Compose dependencies to the Kotlin Multiplatform source sets.
     *
     * Applies the Compose plugins and then configures the Kotlin Multiplatform extension so that:
     * - commonMain receives core Compose libraries (runtime, foundation, material3, ui, icons, components, tooling preview),
     * - androidMain receives the Compose preview dependency,
     * - jvmMain receives the platform-specific desktop dependency for the current OS.
     *
     * @param target The Gradle project to configure.
     */
    override fun apply(target: Project) = with(target) {
        with(pluginManager){
            listOf(
                "composeMultiplatform",
                "composeCompiler",
                "composeHotReload",
            ).forEach { id ->
                pluginManager.apply(libs.findPlugin(id).get().get().pluginId)
            }
        }

        val composeDeps = extensions.getByType<ComposeExtension>().dependencies

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(composeDeps.runtime)
                        implementation(composeDeps.foundation)
                        implementation(composeDeps.material3)
                        implementation(composeDeps.ui)
                        implementation(composeDeps.materialIconsExtended)
                        implementation(composeDeps.components.resources)
                        implementation(composeDeps.material)
                        implementation(composeDeps.components.uiToolingPreview)
                    }
                }
                androidMain.dependencies {
                    implementation(composeDeps.preview)
                }
                jvmMain.dependencies {
                    implementation(composeDeps.desktop.currentOs)
                }
            }
        }
    }
}