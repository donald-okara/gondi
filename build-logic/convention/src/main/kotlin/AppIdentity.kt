import org.gradle.api.Project

data class AppIdentity(
    val appName: String,
    val packagePrefix: String,
    val mainPackageName: String
) {
    val packageName: String
        get() = "$packagePrefix.$mainPackageName"
}

val Project.appIdentity: AppIdentity
    get() = AppIdentity(
        appName = findProperty("appName")?.toString()
            ?: error("APP_NAME not defined in gradle.properties"),
        packagePrefix = findProperty("appPackagePrefix")?.toString()
            ?: error("APP_PACKAGE_PREFIX not defined in gradle.properties"),
        mainPackageName = findProperty("appPackageName")?.toString()
            ?: error("MAIN_PACKAGE_NAME not defined in gradle.properties")
    )