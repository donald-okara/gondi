package com.example.app

import appIdentity
import com.example.app.extensions.coreModules
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DatasourceConvention : Plugin<Project>{
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("${appIdentity.packageName}.kotlinMultiplatformLibrary")

        dependencies {
            coreModules.all.forEach {
                add("implementation", it)
            }

        }

    }
}