package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetInstalledAppIds
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetInstalledAppName
import java.util.Date
import javax.inject.Inject

@AppScope
class AppsRepository @Inject constructor(
    private val appsDao: AppsDao,
    private val appsFetcher: AppsFetcher,
    private val getInstalledAppIds: GetInstalledAppIds,
    private val getAppIdFromLink: GetAppIdFromLink,
    private val getInstalledAppName: GetInstalledAppName,
) {

    fun getApps(): Flow<List<AppModel>> {
        return appsDao.getAllAppsFlow()
    }

    suspend fun updateData() {
        val installedAppIds = getInstalledAppIds()

        updateAppsInfo(installedAppIds)
        addRemoteAppsToDb(installedAppIds)
        updateAppsInfo(installedAppIds)
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
                        appName = remoteAppModel.appName,
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

            val appName = if (appModel.appName.isEmpty() && isAppInstalled) {
                getInstalledAppName(appId = appModel.appId)
            } else {
                appModel.appName
            }

            val installedDate = if (isAppInstalled && appModel.installDate == null) {
                currentDate.time
            } else {
                appModel.installDate
            }

            if (appModel.appName != appName || appModel.isInstalled != isAppInstalled) {
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

        if (appsDao.getAppModelById(appId) != null) {
            return
        }

        appsDao.add(
            AppModel(
                appName = "",
                appId = appId,
                appLink = appLink,
                canBeDeleted = false,
            )
        )
    }
}
