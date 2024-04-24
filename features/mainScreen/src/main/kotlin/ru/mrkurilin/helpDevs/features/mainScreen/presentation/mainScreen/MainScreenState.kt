package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel

data class MainScreenState(
    val isLoading: Boolean = true,
    val idsToInstall: List<AppModel> = listOf(),
    val idsToDelete: List<AppModel> = listOf(),
    val allIds: List<AppModel> = listOf(),
    val showInfo: Boolean = false,
)
