package ke.don.gondi

import ke.don.gondi.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorSupabasePlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                jvmMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-cio").get())
                }
                androidMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-cio").get())
                }
                iosMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-darwin").get())
                }
                commonMain.dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("supabase-bom").get()))
                    implementation(libs.findLibrary("supabase-postgrest").get())
                    implementation(libs.findLibrary("supabase-auth").get())
                    implementation(libs.findLibrary("supabase-realtime").get())
                }
            }
        }
    }
}