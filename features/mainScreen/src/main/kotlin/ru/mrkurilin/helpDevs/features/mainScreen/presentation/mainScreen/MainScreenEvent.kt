package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

sealed interface MainScreenEvent {
    data class AddedAppToInstall(val appLink: String) : MainScreenEvent
}
