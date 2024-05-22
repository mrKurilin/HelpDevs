package ru.mrkurilin.helpDevs.features.mainScreen.presentation.model

import ru.mrkurilin.helpDevs.mainScreen.R

enum class AppsSort(val titleId: Int) {
    NAME(titleId = R.string.by_name),
    INSTALL_DATE(titleId = R.string.by_install_date),
    APP_ID(titleId = R.string.by_id),
    INSTALL_DURATION(titleId = R.string.by_install_duration),
}
