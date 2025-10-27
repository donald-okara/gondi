plugins {
    `kotlin-dsl`
}
val moduleName = "com.example.app" //your module name
group = "$moduleName.buildlogic" //your module name
gradlePlugin {
    plugins {
        register("kotlinMultiplatformLibrary"){
            id = "$moduleName.kotlinMultiplatformLibrary"
            implementationClass = "$moduleName.KotlinMultiplatformLibrary"
        }
        register("kotlinMultiplatformApplication"){
            id = "$moduleName.kotlinMultiplatformApplication"
            implementationClass = "$moduleName.KotlinMultiplatformApplication"
        }
        register("composeMultiplatformPlugin"){
            id = "$moduleName.composeMultiplatformPlugin"
            implementationClass = "$moduleName.ComposeMultiplatformPlugin"
        }
        register("featureConvention"){
            id = "$moduleName.featureConvention"
            implementationClass = "$moduleName.FeatureConvention"
        }
        register("datasourceConvention"){
            id = "$moduleName.datasourceConvention"
            implementationClass = "$moduleName.DatasourceConvention"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}
