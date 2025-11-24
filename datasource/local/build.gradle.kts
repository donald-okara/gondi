plugins {
    alias(libs.plugins.datasourceConvention)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kotlin.serialization)
}

sqldelight {
    databases {
        create("GondiDatabase") {
            packageName.set("ke.don.local.db")

            schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
            migrationOutputDirectory = file("src/commonMain/sqldelight/migrations")
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlin.coroutines)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.bundles.datastore)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.ios)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite)
        }
        jvmTest.dependencies {
            implementation(libs.junit)
            implementation(libs.sqldelight.sqlite)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
        }
    }
}
