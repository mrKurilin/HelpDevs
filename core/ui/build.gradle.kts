plugins {
    `android-setup`
}

android {
    namespace = ProjectConfig.namespace("ui")
}

dependencies {
    implementation(libs.glide)
}