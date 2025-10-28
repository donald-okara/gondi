package ke.don.gondi.extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureVoyager() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.apply {
            commonMain.dependencies {
                implementation(libs.findBundle("voyager").get())
            }
        }
    }
}