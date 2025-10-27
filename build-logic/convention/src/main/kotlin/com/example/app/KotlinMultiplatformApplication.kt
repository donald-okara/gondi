package com.example.app

import appIdentity
import com.android.build.api.dsl.ApplicationExtension
import com.example.app.extensions.configureKotlinAndroid
import com.example.app.extensions.configureKotlinMultiplatform
import com.example.app.extensions.featureModules
import com.example.app.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformApplication: Plugin<Project> {
    override fun apply(target: Project):Unit = with(target){
        with(pluginManager){
            listOf(
                "kotlinMultiplatform",
                "androidApplication",
            ).forEach { id ->
                pluginManager.apply(libs.findPlugin(id).get().get().pluginId)
            }

            apply("${appIdentity.packageName}.composeMultiplatformPlugin")
        }

        dependencies {
            featureModules.all.forEach {
                add("implementation", it)
            }
        }
        extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
        extensions.configure<ApplicationExtension>(::configureKotlinAndroid)
    }
}