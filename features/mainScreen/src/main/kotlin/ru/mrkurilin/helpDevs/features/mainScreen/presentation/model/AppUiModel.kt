package ru.mrkurilin.helpDevs.features.mainScreen.presentation.model

data class AppUiModel(
    val appId: String,
    val appName: String,
    val appLink: String,
    val canBeDeleted: Boolean,
    val isInstalled: Boolean = false,
    val appInstalledDurationDays: Int = 0,
    val installDate: Long? = null,
)
