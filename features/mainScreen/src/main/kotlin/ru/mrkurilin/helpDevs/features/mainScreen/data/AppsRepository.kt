package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppScope
class AppsRepository @Inject constructor(
    private val appsDao: AppsDao,
    private val appsFetcher: AppsFetcher,
    private val isAppInstalledUseCase: IsAppInstalledUseCase,
) {

    suspend fun updateData() {
        val remoteApps = appsFetcher.fetchApps()

        val localAppIds = appsDao.getAllAppIds()

        remoteApps.forEach { remoteApp ->
            if (remoteApp.canBeDeleted && !localAppIds.contains(remoteApp.appId)) {
                return@forEach
            }

            if (!localAppIds.contains(remoteApp.appId)) {
                appsDao.add(remoteApp)
                return@forEach
            }

            val localAppModel = appsDao.getAppModelById(remoteApp.appId)

            if (localAppModel.canBeDeleted) {
                return@forEach
            }

            if (localAppModel.installDate != null) {
                return@forEach
            }

            val isRemoteAppInstalled = isAppInstalledUseCase(remoteApp.appId)
            if (isRemoteAppInstalled) {
                appsDao.update(
                    localAppModel.copy(
                        installDate = Date().time
                    )
                )
                return@forEach
            }

            appsDao.add(remoteApp)
        }
    }

    fun getApps(): Flow<List<AppModel>> {
        val currentDate = Date()
        return appsDao.getAllApps().map { appModels ->
            appModels.map { appModel ->
                val isInstalled = isAppInstalledUseCase(appModel.appId)

                val installDate = if (isInstalled && appModel.installDate == null) {
                    currentDate.time
                } else {
                    appModel.installDate
                }

                val canBeDeleted = if (!appModel.canBeDeleted && isInstalled) {
                    isInstalledForTwoWeeks(installDate, currentDate.time)
                } else {
                    appModel.canBeDeleted
                }

                appModel.copy(
                    isInstalled = isInstalled,
                    canBeDeleted = canBeDeleted,
                    installDate = installDate,
                )
            }.sortedBy { it.appId }
        }
    }

    private fun isInstalledForTwoWeeks(
        installDateTime: Long?,
        currentDateTime: Long,
    ): Boolean {
        if (installDateTime == null) {
            return false
        }

        val diffInMillisec: Long = currentDateTime - installDateTime

        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diffInMillisec)

        return diffInDays > 14

    }

    suspend fun changeCanBeDeleted(appId: String) {
        val localAppModel = appsDao.getAppModelById(appId)
        appsDao.update(localAppModel.copy(canBeDeleted = !localAppModel.canBeDeleted))
    }
}
