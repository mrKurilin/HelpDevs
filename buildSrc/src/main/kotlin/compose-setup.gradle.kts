plugins {
    id("com.android.library")
}

internal val Project.libs: VersionCatalog
    get() = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
    }
}

dependencies {
    implementation(libs.findBundle("compose").get())
    implementation(libs.findLibrary("glide").get())
}
