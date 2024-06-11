plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

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
        release {
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
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:di"))
    implementation(project(":features:mainScreen"))

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.navigationFragmentKtx)
    implementation(libs.dagger)
    ksp(libs.daggerCompiler)

    implementation(libs.kotlinSerializationCore)
    implementation(libs.kotlinSerializationJson)
    implementation(libs.bundles.network)

    implementation(libs.coreSplashscreen)

    implementation(libs.room)
    ksp(libs.roomCompiler)

    implementation(libs.composeUi)
}
