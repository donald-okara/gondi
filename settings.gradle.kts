val appName: String by settings

rootProject.name = appName
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// App
include(":composeApp")
includeBuild("build-logic")

// Folders
include(":core")
include(":feature")
include(":datasource")
include(":shared")

// Core
include(":core:domain")
include(":core:utils")

// Datasource
include(":datasource:remote")
include(":datasource:local")

// Feature

// Shared UI
include(":shared:design")
include(":shared:resources")
include(":shared:components")
include(":feature:authentication")
include(":feature:home")
include(":feature:gamePlay")
