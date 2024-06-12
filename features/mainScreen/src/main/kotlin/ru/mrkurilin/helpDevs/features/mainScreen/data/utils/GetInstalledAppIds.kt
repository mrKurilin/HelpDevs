package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import android.content.pm.PackageManager
import org.koin.core.annotation.Single

@Single
class GetInstalledAppIds(
    private val context: Context,
) {

    operator fun invoke(): List<String> {
        return context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA).map {
            it.packageName
        }
    }
}
