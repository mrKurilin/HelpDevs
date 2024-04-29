package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import android.content.pm.PackageManager
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import javax.inject.Inject

class IsAppInstalled @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    operator fun invoke(appId: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(appId, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
