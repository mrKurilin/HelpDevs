package ru.mrkurilin.helpDevs.features.mainScreen.presentation.mainScreen

import ru.mrkurilin.helpDevs.mainScreen.R

enum class MainScreenTabs(
    val titleId: Int
) {
    CAN_BE_INSTALLED(titleId = R.string.can_be_installed),
    CAN_BE_DELETED(titleId = R.string.can_be_deleted),
    ALL_APPS(titleId = R.string.all_apps),
}
