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

buildConfig {
    packageName.set("ke.don.remote")

    buildConfigField("SUPABASE_URL", "${keys["SUPABASE_URL"]}")
    buildConfigField("SUPABASE_KEY", "${keys["SUPABASE_KEY"]}")
}