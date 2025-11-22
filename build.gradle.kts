import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.kotzilla) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.kover)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.jetbrains.kotlinx.kover")
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory.get().asFile}/**/*.kt")
            ktlint("0.50.0")
                .editorConfigOverride(
                    mapOf(
                        "ktlint_standard_package-name" to "disabled",
                        "ktlint_standard_no-wildcard-imports" to "disabled",
                        "ktlint_standard_class-naming" to "disabled"
                    )
                )
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint("1.7.0")
        }
    }
}

kover {
    reports {
        total {
            xml {
                onCheck = true
                xmlFile = layout.projectDirectory.file("kover.xml")
            }
        }
        filters {
            excludes {
                androidGeneratedClasses()
                classes("**/build/**")
            }
        }
        verify {
            rule {
                bound {
                    minValue = 50
                    maxValue = 75
                }
            }
        }
    }
}
