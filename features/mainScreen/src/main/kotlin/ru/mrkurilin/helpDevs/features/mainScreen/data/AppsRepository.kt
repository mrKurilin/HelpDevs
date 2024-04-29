package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetInstalledAppName
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.IsAppInstalled
import java.util.Date
import javax.inject.Inject

@AppScope
class AppsRepository @Inject constructor(
    private val appsDao: AppsDao,
    private val appsFetcher: AppsFetcher,
    private val isAppInstalled: IsAppInstalled,
    private val getAppIdFromLink: GetAppIdFromLink,
    private val getInstalledAppName: GetInstalledAppName,
) {

    fun getApps(): Flow<List<AppModel>> {
        return appsDao.getAllApps()
    }

    suspend fun updateData() {
        val remoteApps = appsFetcher.fetchApps()
        val localAppIds = appsDao.getAllAppIds()

        remoteApps.forEach { remoteAppModel ->
            val isAppInstalled = isAppInstalled(remoteAppModel.appId)
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

        appsDao.getAllAppsByName("").forEach { unnamedApp ->
            if (isAppInstalled(unnamedApp.appId)) {
                appsDao.add(
                    unnamedApp.copy(
                        appName = getInstalledAppName(unnamedApp.appId) ?: "",
                        isInstalled = isAppInstalled(unnamedApp.appId),
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
