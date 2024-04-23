package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
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

    fun getApps(): Flow<List<AppModel>> {
        return appsDao.getAllApps()
    }

    suspend fun updateData() {
        val remoteApps = appsFetcher.fetchApps()
        val localAppIds = appsDao.getAllAppIds()

        remoteApps.forEach { remoteAppModel ->
            val isRemoteAppInstalled = isAppInstalledUseCase(remoteAppModel.appId)
            val isSavedLocal = localAppIds.contains(remoteAppModel.appId)

            if (remoteAppModel.canBeDeleted && !isRemoteAppInstalled && !isSavedLocal) {
                return@forEach
            }

            val currentDate = Date()

            if (!isSavedLocal) {
                appsDao.add(
                    remoteAppModel.copy(
                        canBeDeleted = remoteAppModel.canBeDeleted,
                        isInstalled = isRemoteAppInstalled,
                        appearanceDate = currentDate.time,
                    )
                )
            }

            val localAppModel = appsDao.getAppModelById(remoteAppModel.appId)

            val installDate = if (localAppModel.installDate == null && isRemoteAppInstalled) {
                currentDate.time
            } else localAppModel.installDate

            val isInstalledForTwoWeeks = isInstalledForTwoWeeks(
                installDate,
                currentDate.time
            )

            val appearanceDate = localAppModel.appearanceDate ?: currentDate.time

            val canBeDeleted =
                localAppModel.canBeDeleted || remoteAppModel.canBeDeleted || isInstalledForTwoWeeks

            appsDao.add(
                localAppModel.copy(
                    canBeDeleted = canBeDeleted,
                    isInstalled = isRemoteAppInstalled,
                    appearanceDate = appearanceDate,
                    installDate = installDate,
                )
            )
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
