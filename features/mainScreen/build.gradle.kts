plugins {
    `android-setup`
    `compose-setup`
}

android {
    namespace = ProjectConfig.namespace("mainScreen")
}

dependencies {
    implementation(libs.jsoup)

    implementation(libs.room)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)

    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    implementation("io.insert-koin:koin-annotations:1.3.1")
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")
}
