@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `android-setup`
    id(libs.plugins.navSafeArgsPlugin.get().pluginId)
}

android {
    namespace = ProjectConfig.namespace("navigation")
    sourceSets["main"].java.srcDir(file("build/generated/nav"))
}

dependencies {
    implementation(libs.navigationFragmentKtx)
}