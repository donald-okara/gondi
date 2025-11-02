package ke.don.gondi.extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureKoin() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.apply {
            commonMain.dependencies {
                implementation(libs.findLibrary("koin-core").get())
                implementation(libs.findLibrary("koin-compose").get())
            }

            androidMain.dependencies {
                implementation(libs.findLibrary("koin-android").get())
                implementation(libs.findLibrary("koin-androidx-compose").get())
                implementation(libs.findLibrary("koin-compose-viewmodel").get())
            }
        }
    }
}
