package ke.don.gondi.extensions

import appIdentity
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    extension: CommonExtension<*, *, *, * ,* , *>
) = extension.apply {
    namespace = if (moduleName == "composeApp" || moduleName.isEmpty()) {
        appIdentity.packageName
    } else {
        "${appIdentity.packagePrefix}.$moduleName"
    }

    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
    defaultConfig {
        minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
    }
    if (this is ApplicationExtension) {
        defaultConfig {
            targetSdk = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        getByName("release") {
            if (this@apply is ApplicationExtension) {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            } else {
                isMinifyEnabled = false
            }
        }
    }

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}
