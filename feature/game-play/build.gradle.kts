plugins {
    alias(libs.plugins.featureConvention)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:home"))
        }
    }
}
