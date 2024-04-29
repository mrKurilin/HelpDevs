package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModel

data class MainScreenState(
    val isLoading: Boolean = true,
    val appsToInstall: List<AppUiModel> = listOf(),
    val appsToDelete: List<AppUiModel> = listOf(),
    val allApps: List<AppUiModel> = listOf(),
    val showInfo: Boolean = false,
)
