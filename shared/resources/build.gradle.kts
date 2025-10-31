plugins {
    alias(libs.plugins.kotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatformPlugin)
}

compose {
    resources {
        publicResClass = true // generates Res.*
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(path = ":core:domain"))
            implementation(project(path = ":core:utils"))
        }
    }

}
