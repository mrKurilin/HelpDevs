package ru.mrkurilin.helpDevs.features.mainScreen.presentation.model

import org.koin.core.annotation.Single
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import java.util.Date
import java.util.concurrent.TimeUnit

@Single
class AppUiModelMapper {

    fun mapFromAppModel(appModel: AppModel): AppUiModel {
        val installedDurationDays = if (appModel.isInstalled && appModel.installDate != null) {
            TimeUnit.MILLISECONDS.toDays(
                Date().time - appModel.installDate
            ).toInt()
        } else {
            0
        }

        return AppUiModel(
            appId = appModel.appId.lowercase(),
            appName = appModel.appName,
            appLink = appModel.appLink,
            canBeDeleted = appModel.canBeDeleted,
            isInstalled = appModel.isInstalled,
            installDate = appModel.installDate,
            appInstalledDurationDays = installedDurationDays,
        )
    }
}
