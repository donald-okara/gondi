import java.util.Properties

plugins {
    alias(libs.plugins.featureConvention)
    alias(libs.plugins.gmazzoBuildConfig)
}

val identityFile = rootProject.file("gradle.properties")
val identityProperties = Properties()
if (identityFile.exists()) {
    identityFile.inputStream().use { identityProperties.load(it) }
}

val versionProperty =
    identityProperties["version"]?.toString()
        ?: error("Vesrion not found in gradle.properties")
val versionNameProperty =
    identityProperties["versionName"]?.toString()
        ?: error("Version name not found in gradle.properties")

buildConfig {
    packageName.set("ke.don.home")

    buildConfigField("VERSION_CODE", versionProperty)
    buildConfigField("VERSION_NAME", versionNameProperty)
}
