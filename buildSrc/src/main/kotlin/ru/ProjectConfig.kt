import org.gradle.api.JavaVersion

/**
 * Provides android project config
 */
object ProjectConfig {
    const val namespace = "ru.mrkurilin.helpDevs"
    fun namespace(name: String? = null) = "$namespace.$name"

    const val compileSdk = 34
    const val applicationId = namespace
    const val minSdk = 24
    const val targetSdk = 34
    const val versionCode = 1
    const val versionName = "1.0"

    val javaVersion = JavaVersion.VERSION_17
    const val jvmTarget = "17"
}
