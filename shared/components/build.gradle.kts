plugins {
    alias(libs.plugins.kotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatformPlugin)
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            api(libs.bundles.compose)
            implementation(project(":shared:resources"))
            implementation(project(":shared:design"))
        }
    }
}