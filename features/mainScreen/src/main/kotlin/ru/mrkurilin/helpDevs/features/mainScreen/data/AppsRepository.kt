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
    private val getAppName: GetInstalledAppName,
) {

    fun getApps(): Flow<List<AppModel>> {
        return appsDao.getAllAppsFlow()
    }

    suspend fun updateData() {
        val remoteApps = appsFetcher.fetchApps()
        val localAppIds = appsDao.getAllAppIds()
        val installedAppIds = getInstalledAppIds()

        remoteApps.forEach { remoteAppModel ->
            val isAppInstalled = installedAppIds.contains(remoteAppModel.appId)
            val isSavedLocal = localAppIds.contains(remoteAppModel.appId)

            if (remoteAppModel.canBeDeleted && !isAppInstalled && !isSavedLocal) {
                return@forEach
            }

            val currentDate = Date()

            if (!isSavedLocal) {
                appsDao.add(
                    remoteAppModel.copy(
                        isInstalled = isAppInstalled,
                        installDate = if (isAppInstalled) currentDate.time else null,
                    )
                )
            }

            val localAppModel = appsDao.getAppModelById(remoteAppModel.appId) ?: return@forEach

            val installDate = if (isAppInstalled && localAppModel.installDate == null) {
                currentDate.time
            } else localAppModel.installDate

            val canBeDeleted = localAppModel.canBeDeleted || remoteAppModel.canBeDeleted

            appsDao.add(
                localAppModel.copy(
                    canBeDeleted = canBeDeleted,
                    isInstalled = isAppInstalled,
                    installDate = installDate,
                )
            )
        }

        appsDao.getAllAppsList().forEach { appModel ->
            val isAppInstalled = installedAppIds.contains(appModel.appId)

            val appName = appModel.appName.ifEmpty {
                getAppName(appModel.appName)
            }

            if (appModel.appName != appName || appModel.isInstalled != isAppInstalled) {
                appsDao.update(
                    appModel.copy(
                        appName = appName,
                        isInstalled = isAppInstalled,
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
