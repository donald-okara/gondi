plugins {
    alias(libs.plugins.datasourceConvention)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kotlin.serialization)
}

sqldelight {
    databases {
        create("GondiDatabase") {
            packageName.set("ke.don.local.db")
        }
    }
}


kotlin{
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqldelight.runtime)
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
    }
}