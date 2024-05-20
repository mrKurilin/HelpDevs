package ru.mrkurilin.helpDevs.features.mainScreen.presentation.state

sealed interface MainScreenEvent {
    data class AddedAppToInstall(val appLink: String) : MainScreenEvent
}
