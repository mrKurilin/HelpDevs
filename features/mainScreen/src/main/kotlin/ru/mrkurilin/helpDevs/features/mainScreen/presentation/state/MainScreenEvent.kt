package ru.mrkurilin.helpDevs.features.mainScreen.presentation.state

sealed interface MainScreenEvent {
    data class AddedAppToInstall(val appLink: String) : MainScreenEvent
    data object InternetConnectionError : MainScreenEvent
    data object UnknownError : MainScreenEvent
}
