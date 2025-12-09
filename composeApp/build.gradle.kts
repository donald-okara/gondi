import io.kotzilla.gradle.ext.KotzillaKeyGeneration
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatformApplication)
    alias(libs.plugins.ktorSupabasePlugin)
    alias(libs.plugins.kotzilla)
}

val identityFile = rootProject.file("gradle.properties")
val identityProperties = Properties()
if (identityFile.exists()) {
    identityProperties.load(identityFile.inputStream())
}

val versionProperty =
    identityProperties["version"]?.toString()
        ?: error("Vesrion not found in local.properties")
val versionNameProperty =
    identityProperties["versionName"]?.toString()
        ?: error("Version name not found in local.properties")

kotlin {

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

compose {
    resources {
        publicResClass = true // generates Res.*
    }
}

android {
    defaultConfig {
        versionCode = versionProperty.toInt()
        versionName = versionNameProperty
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.voyager)
            implementation(libs.ui.backhandler)
            implementation(libs.ui.backhandler)
        }
        androidMain.dependencies {
            implementation(libs.kotzilla.sdk.compose)
        }
        iosMain.dependencies {
            implementation(libs.kotzilla.sdk.compose)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "ke.don.gondi.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ke.don.gondi"
            packageVersion = "1.0.0"
        }
    }
}

kotzilla {
    versionName = "1.0" // add your app version name
    keyGeneration = KotzillaKeyGeneration.COMPOSE
    composeInstrumentation = true
}
