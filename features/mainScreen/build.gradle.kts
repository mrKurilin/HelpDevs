plugins {
    `android-setup`
    `compose-setup`
}

android {
    namespace = ProjectConfig.namespace("mainScreen")
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:di"))
    implementation(project(":core:navigation"))

    implementation(libs.jsoup)

    implementation(libs.room)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)
}
