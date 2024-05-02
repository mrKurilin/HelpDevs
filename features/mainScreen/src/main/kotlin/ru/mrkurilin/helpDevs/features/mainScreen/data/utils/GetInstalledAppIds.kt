package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import android.content.pm.PackageManager
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import javax.inject.Inject

class GetInstalledAppIds @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    operator fun invoke(): List<String> {
        return context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA).map {
            it.packageName
        }
    }
}
