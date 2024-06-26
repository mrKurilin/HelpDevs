import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

val properties = Properties().apply {
    rootProject.file("local.properties").reader().use(::load)
}
val TLG_BOT_TOKEN = properties["TLG_BOT_TOKEN"] as String

android {
    compileSdk = 34
    namespace = ProjectConfig.namespace
    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName
    }

    buildTypes {
        debug {
            buildConfigField("String", "TLG_BOT_TOKEN", TLG_BOT_TOKEN)
        }
        release {
            buildConfigField("String", "TLG_BOT_TOKEN", TLG_BOT_TOKEN)
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    // For KSP
    applicationVariants.configureEach {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${this@configureEach.name}/kotlin")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    ksp {
        arg("KOIN_CONFIG_CHECK","true")
    }
}

dependencies {
    implementation(project(":features:mainScreen"))

    implementation(libs.bundles.compose)

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.navigationFragmentKtx)

    implementation(libs.kotlinSerializationCore)
    implementation(libs.kotlinSerializationJson)
    implementation(libs.bundles.network)

    implementation(libs.coreSplashscreen)

    implementation(libs.roomKtx)
    implementation(libs.room)
    ksp(libs.roomCompiler)

    implementation(libs.composeUi)

    implementation(libs.junit)

    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-annotations:1.3.1")
    implementation("io.insert-koin:koin-test:3.5.6")
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")

    implementation(libs.telegramBot)
}
