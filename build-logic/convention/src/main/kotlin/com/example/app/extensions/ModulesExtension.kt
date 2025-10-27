package com.example.app.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.moduleName: String
    get() = path.split(":").drop(1).joinToString(".")

val Project.coreModules: CoreModules
    get() = CoreModules(this)

class CoreModules(private val project: Project) {
    val domain get() = project.project(":core:domain")
    val utils get() = project.project(":core:utils")

    val all get() = listOf(domain, utils)
}

val Project.featureModules: FeatureModules
    get() = FeatureModules(this)

class FeatureModules(private val project: Project) {
    // Your feature module references
    // e.g
    // val auth get() = project.project(":feature:auth"))
    val all: List<Project> = emptyList()
}

val Project.sharedModules: SharedModules
    get() = SharedModules(this)

class SharedModules(private val project: Project) {
    val design get() = project.project(":shared:design")
    val resources get() = project.project(":shared:resources")
    val common get() = project.project(":shared:components")

    val all get() = listOf(design, resources, common)
}

val Project.datasourceModules: DatasourceModules
    get() = DatasourceModules(this)

class DatasourceModules(private val project: Project) {
    val remote get() = project.project(":datasource:remote")
    val local get() = project.project(":datasource:local")

    val all get() = listOf(remote, local)
}