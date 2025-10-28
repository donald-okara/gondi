import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.datasourceConvention)
    alias(libs.plugins.ktorSupabasePlugin)
    id("com.codingfeline.buildkonfig") version "0.17.1"
}

val keysFile = rootProject.file("local.properties")
val keys = Properties()
if (keysFile.exists()) {
    keys.load(keysFile.inputStream())
}

buildkonfig {
    packageName = "ke.don.remote"

    defaultConfigs {
        buildConfigField(STRING, "SUPABASE_URL", "${keys["SUPABASE_URL"]}")
        buildConfigField(STRING, "SUPABASE_KEY", "${keys["SUPABASE_KEY"]}")
    }
}