import java.util.Properties

plugins {
    alias(libs.plugins.datasourceConvention)
    alias(libs.plugins.ktorSupabasePlugin)
    id("com.github.gmazzo.buildconfig") version "5.7.0"
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
