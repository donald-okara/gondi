# Plugins documentation

This is a detailed documentation of the plugins `KleanBoy` comes with.
### a. kotlinMultiplatformLibrary

```kotlin
register("kotlinMultiplatformLibrary"){  
    id = "$moduleName.kotlinMultiplatformLibrary"  
    implementationClass = "$moduleName.KotlinMultiplatformLibrary"  
}
```

This is the plugin that comes with the base configuration for a Kotlin Multiplatform (KMP) **library**.

It includes plugins `kotlinMultiplatform` and `androidLibrary` and configures extensions `KotlinMultiplatformExtension` and `LibraryExtension` through `configureKotlinMultiplatform` and `configureKotlinAndroid` respectively

#### i. `Project.configureKotlinMultiplatform

This extension adds the targets for `Android`, `jvm` and `iOS`.

It dynamically maps baseName for iOS targets

``` kotlin
listOf(  
    iosX64(),  
    iosArm64(),  
    iosSimulatorArm64(),  
).forEach { iosTarget ->  
    iosTarget.binaries.framework {  
        baseName = moduleName.replace(".", "_") + "_Kit"  
        isStatic = true  
    }  
}
```

#### ii. `Project.configureKotlinAndroid

This extension configures an Android module catering for both `android libraries` and `android application`.

It dynamically maps module package namespace

```kotlin
namespace = if (moduleName == "composeApp" || moduleName.isEmpty()) {  
    appIdentity.packageName  
} else {  
    "${appIdentity.packagePrefix}.$moduleName"  
}
```

### b. kotlinMultiplatformApplication

```kotlin
register("kotlinMultiplatformApplication"){  
    id = "$moduleName.kotlinMultiplatformApplication"  
    implementationClass = "$moduleName.KotlinMultiplatformApplication"  
}
```

This is the plugin that comes with the base configuration for a Kotlin Multiplatform (KMP) **application**.

It includes plugins `kotlinMultiplatform` and `androidLibrary` and configures extensions `KotlinMultiplatformExtension` and `LibraryExtension` through `configureKotlinMultiplatform` and `configureKotlinAndroid` respectively as seen above.

It also implements the feature modules by default. See `build-logic/convention/src/main/kotlin/com/example/app/extensions/ModulesExtension.kt` for project modules bundles.

> **Warning:** This plugin should only be used once in the project. In the module with the `MainActivity`

### c. composeMultiplatformPlugin

```kotlin
register("composeMultiplatformPlugin"){  
    id = "$moduleName.composeMultiplatformPlugin"  
    implementationClass = "$moduleName.ComposeMultiplatformPlugin"  
}
```

It includes plugins `composeMultiplatform` , `composeHotReload`  `composeCompiler` as well as adds the compose libraries to sourcesets

```kotlin
  
val composeDeps = extensions.getByType<ComposeExtension>().dependencies  
  
extensions.configure<KotlinMultiplatformExtension> {  
    sourceSets.apply {  
        commonMain {  
            dependencies {  
                implementation(composeDeps.runtime)  
                implementation(composeDeps.foundation)  
                implementation(composeDeps.material3)  
                implementation(composeDeps.ui)  
                implementation(composeDeps.materialIconsExtended)  
                implementation(composeDeps.components.resources)  
                implementation(composeDeps.material)  
                implementation(composeDeps.components.uiToolingPreview)  
            }  
        }        jvmMain.dependencies {  
            implementation(composeDeps.desktop.currentOs)  
        }  
    }}
```

> This plugin can be used in any module that has UI. It is included by default in `kotlinMultiplatformApplication` and `featureConvention`.


### d. featureConvention

```kotlin
register("featureConvention"){  
    id = "$moduleName.featureConvention"  
    implementationClass = "$moduleName.FeatureConvention"  
}
```

It includes plugins `composeMultiplatformPlugin` and `kotlinMultiplatformLibrary` above as well as implements `datasource`, `core` and `shared` modules.

### e. datasourceConvention

```kotlin
register("datasourceConvention"){  
    id = "$moduleName.datasourceConvention"  
    implementationClass = "$moduleName.DatasourceConvention"  
}
```

It includes plugins `kotlinMultiplatformLibrary`as well as implements  `core` modules.

## Relationships between plugins
- `kotlinMultiplatformLibrary` → base setup
- `composeMultiplatformPlugin` → adds UI dependencies
- `featureConvention` → depends on both above
- `datasourceConvention` → depends on core modules
- `kotlinMultiplatformApplication` → entry point of the app
