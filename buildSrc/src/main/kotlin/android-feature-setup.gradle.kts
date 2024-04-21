plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

internal val Project.libs: VersionCatalog
    get() = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk = ProjectConfig.compileSdk
    namespace = ProjectConfig.namespace
    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "isDebug", "false")
        }
        debug {
            buildConfigField("boolean", "isDebug", "true")
        }
    }

    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }

    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
}

dependencies {
    implementation(libs.findLibrary("coreKtx").get())
    implementation(libs.findLibrary("appcompat").get())
    implementation(libs.findLibrary("material").get())

    implementation(libs.findLibrary("coroutines").get())

    implementation(libs.findLibrary("navigationFragmentKtx").get())

    testImplementation(libs.findBundle("tests").get())
    androidTestImplementation(libs.findBundle("androidTests").get())
}
