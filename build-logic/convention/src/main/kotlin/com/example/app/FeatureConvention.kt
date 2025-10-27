package com.example.app

import appIdentity
import com.example.app.extensions.coreModules
import com.example.app.extensions.datasourceModules
import com.example.app.extensions.sharedModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureConvention : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            listOf(
                "kotlinMultiplatformLibrary",
                "composeMultiplatformPlugin"
            ).forEach { id ->
                apply("${appIdentity.packageName}.$id")
            }
        }

        dependencies {
            coreModules.all.forEach {
                add("implementation", it)
            }
            datasourceModules.all.forEach {
                add("implementation", it)
            }
            sharedModules.all.forEach {
                add("implementation", it)
            }
        }

    }
}
