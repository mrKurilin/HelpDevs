package ru.mrkurilin.helpDevs.features.mainScreen.data

import kotlinx.coroutines.flow.Flow
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import java.util.Date
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
            val isAppInstalled = isAppInstalledUseCase(remoteAppModel.appId)
            val isSavedLocal = localAppIds.contains(remoteAppModel.appId)

            if (remoteAppModel.canBeDeleted && !isAppInstalled && !isSavedLocal) {
                return@forEach
            }

            val currentDate = Date()

            if (!isSavedLocal) {
                appsDao.add(
                    remoteAppModel.copy(
                        isInstalled = isAppInstalled,
                        installDate = if (isAppInstalled) currentDate.time else  null,
                    )
                )
            }

            val localAppModel = appsDao.getAppModelById(remoteAppModel.appId)

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
    }

    suspend fun changeCanBeDeleted(appId: String) {
        val localAppModel = appsDao.getAppModelById(appId)
        appsDao.update(localAppModel.copy(canBeDeleted = !localAppModel.canBeDeleted))
    }
}
