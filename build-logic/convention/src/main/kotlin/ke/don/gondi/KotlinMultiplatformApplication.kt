package ke.don.gondi

import appIdentity
import com.android.build.api.dsl.ApplicationExtension
import ke.don.gondi.extensions.configureKotlinAndroid
import ke.don.gondi.extensions.configureKotlinMultiplatform
import ke.don.gondi.extensions.featureModules
import ke.don.gondi.extensions.libs
import ke.don.gondi.extensions.sharedModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformApplication: Plugin<Project> {
    override fun apply(target: Project):Unit = with(target){
        with(pluginManager){
            listOf(
                "kotlinMultiplatform",
                "androidApplication",
            ).forEach { id ->
                pluginManager.apply(libs.findPlugin(id).get().get().pluginId)
            }

            apply("${appIdentity.packageName}.composeMultiplatformPlugin")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain.dependencies {
                    featureModules.all.forEach {
                        implementation(it)
                    }
                    sharedModules.all.forEach{
                        implementation(it)
                    }
                }

            }
        }
        extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
        extensions.configure<ApplicationExtension>(::configureKotlinAndroid)
    }
}