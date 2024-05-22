package ru.mrkurilin.helpDevs.features.mainScreen.presentation.model

import ru.mrkurilin.helpDevs.mainScreen.R

enum class AppsFilter(val titleId: Int) {
    INSTALLED(titleId = R.string.installed),
    UNINSTALLED(titleId = R.string.uninstalled),
}
