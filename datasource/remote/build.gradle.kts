import java.util.Properties

plugins {
    alias(libs.plugins.datasourceConvention)
    alias(libs.plugins.ktorSupabasePlugin)
    alias(libs.plugins.gmazzoBuildConfig)
}

val keysFile = rootProject.file("local.properties")
val keys = Properties()
if (keysFile.exists()) {
    keys.load(keysFile.inputStream())
}

val supabaseUrl =
    keys["SUPABASE_URL"]?.toString()
        ?: error("SUPABASE_URL not found in local.properties")
val supabaseKey =
    keys["SUPABASE_KEY"]?.toString()
        ?: error("SUPABASE_KEY not found in local.properties")

buildConfig {
    packageName.set("ke.don.remote")

    buildConfigField("SUPABASE_URL", supabaseUrl)
    buildConfigField("SUPABASE_KEY", supabaseKey)
}

kotlin {
    sourceSets {

        jvmMain.dependencies {
            implementation(libs.ktor.server.websockets)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.jmdns)
        }
        androidMain.dependencies {
            implementation(libs.ktor.server.websockets)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.jmdns)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlin.coroutines)
            implementation(project(":datasource:local"))
        }
        jvmTest.dependencies {
            implementation(libs.junit)
            implementation(libs.sqldelight.sqlite)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
        }
    }
}
