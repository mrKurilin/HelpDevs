package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Single
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppName
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetInstalledAppIds
import java.util.Date

@Single
class AppsRepository(
    private val appsDao: AppsDao,
    private val appsFetcher: AppsFetcher,
    private val getInstalledAppIds: GetInstalledAppIds,
    private val getAppIdFromLink: GetAppIdFromLink,
    private val getAppName: GetAppName,
) {

    fun getApps(): Flow<List<AppModel>> {
        return appsDao.getAllAppsFlow()
    }

    suspend fun updateData() = supervisorScope {
        val installedAppIds = getInstalledAppIds()

        try {
            addRemoteAppsToDb(installedAppIds)
        } finally {
            updateAppsInfo(installedAppIds)
        }
    }

    private suspend fun addRemoteAppsToDb(installedAppIds: List<String>) {
        val remoteApps = appsFetcher.fetchApps()
        val localApps = appsDao.getAllAppsList()

        remoteApps.forEach forEachRemoteApp@{ remoteAppModel ->
            val isAppInstalled = installedAppIds.contains(remoteAppModel.appId)

            if (remoteAppModel.canBeDeleted && !isAppInstalled) {
                return@forEachRemoteApp
            }

            localApps.forEach { localApp ->
                if (localApp.appId == remoteAppModel.appId) {
                    val updatedApp = localApp.copy(
                        canBeDeleted = localApp.canBeDeleted || remoteAppModel.canBeDeleted,
                        isInstalled = isAppInstalled,
                    )
                    appsDao.update(updatedApp)
                    return@forEachRemoteApp
                }
            }

            val appToAdd = remoteAppModel.copy(isInstalled = isAppInstalled)
            appsDao.add(appToAdd)
        }
    }

    private suspend fun updateAppsInfo(installedAppIds: List<String>) {
        val currentDate = Date()

        appsDao.getAllAppsList().forEach { appModel ->
            val isAppInstalled = installedAppIds.contains(appModel.appId)

            val appName = if (isAppInstalled) {
                getAppName(appModel.appId)
            } else {
                appModel.appName.ifEmpty { getAppName(appModel.appId) }
            }

            val installedDate = if (!isAppInstalled) {
                null
            } else {
                appModel.installDate ?: currentDate.time
            }

            if (
                appModel.isInstalled != isAppInstalled
                ||
                appModel.appName != appName
                ||
                appModel.installDate != installedDate
            ) {
                appsDao.update(
                    appModel.copy(
                        appName = appName,
                        isInstalled = isAppInstalled,
                        installDate = installedDate,
                    )
                )
            }
        }
    }

    suspend fun changeCanBeDeleted(appId: String) {
        val localAppModel = appsDao.getAppModelById(appId) ?: return
        appsDao.update(localAppModel.copy(canBeDeleted = !localAppModel.canBeDeleted))
    }

    suspend fun addApp(appLink: String) {
        val appId = getAppIdFromLink(appLink) ?: return

        val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId

        if (appsDao.getAppModelById(appId) != null) {
            return
        }

        val appModel = AppModel(
            appName = "",
            appId = appId.lowercase(),
            appLink = validAppLink,
            canBeDeleted = false,
        )
        appsDao.add(appModel)
    }

    suspend fun addAppFromTlg(
        appLink: String,
        ownerTlgUserName: String?,
    ) {
        val appId = getAppIdFromLink(appLink) ?: return

        val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId

        val appModelById = appsDao.getAppModelById(appId)

        if (appModelById != null) {
            appsDao.update(appModelById.copy(ownerTlgUserName = ownerTlgUserName))
        } else {
            val appModel = AppModel(
                appName = getAppName(appId),
                appId = appId.lowercase(),
                appLink = validAppLink,
                canBeDeleted = false,
                ownerTlgUserName = ownerTlgUserName,
            )
            appsDao.add(appModel)
        }
    }
}
