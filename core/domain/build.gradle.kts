plugins {
    alias(libs.plugins.kotlinMultiplatformLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
        }
    }
}