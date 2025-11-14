import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatformApplication)
    alias(libs.plugins.ktorSupabasePlugin)
}

kotlin {

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    defaultConfig {
        versionCode = 1
        versionName = "1.0"
    }
}

kotlin{
    sourceSets{
        commonMain.dependencies{
            implementation(libs.bundles.voyager)
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
