package ke.don.gondi

import ke.don.gondi.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorSupabasePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                // Ensure base source sets exist
                val commonMain = maybeCreate("commonMain")
                val jvmMain = maybeCreate("jvmMain")
                val androidMain = maybeCreate("androidMain")
                val iosMain = maybeCreate("iosMain")

                // --- Create shared JVM layer ---
                val jvmSharedMain = maybeCreate("jvmSharedMain").apply {
                    dependsOn(commonMain)
                }

                // JVM + Android depend on shared JVM layer
                jvmMain.dependsOn(jvmSharedMain)
                androidMain.dependsOn(jvmSharedMain)

                // --- Dependencies ---
                jvmSharedMain.dependencies {
                    // Shared Ktor client/server deps
                    implementation(libs.findLibrary("ktor-server-core").get())
                    implementation(libs.findLibrary("ktor-server-cio").get())
                    implementation(libs.findLibrary("ktor-server-content-negotiation").get())
                    implementation(libs.findLibrary("ktor-serialization-kotlinx-json").get())
                    implementation(libs.findLibrary("ktor-server-netty").get())
                    implementation(libs.findLibrary("ktor-server-websockets").get())
                    implementation(libs.findLibrary("jmdns").get())
                }

                jvmMain.dependencies {
                    // JVM-specific server bits
                    implementation(libs.findLibrary("ktor-client-cio").get())
                }
                androidMain.dependencies {
                    // JVM-specific server bits
                    implementation(libs.findLibrary("ktor-client-cio").get())
                }

                iosMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-darwin").get())
                }

                commonMain.dependencies {
                    api(project.dependencies.platform(libs.findLibrary("supabase-bom").get()))
                    api(libs.findLibrary("supabase-auth").get())
                    api(libs.findLibrary("supabase-realtime").get())
                    implementation(libs.findLibrary("supabase-postgrest").get())
                }
            }
        }
    }
}
