package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import org.koin.core.annotation.Single
import java.util.Locale

@Single
class GetAppName(
    private val context: Context,
) {

    operator fun invoke(appId: String): String {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(appId, 0)
            val label = context.packageManager.getApplicationLabel(applicationInfo).toString()

            label.ifEmpty {
                getAppNameByAppId(appId)
            }
        } catch (e: Exception) {
            getAppNameByAppId(appId)
        }
    }

    private fun getAppNameByAppId(appId: String): String {
        return appId.removeSuffix(".app").split(".").last().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
