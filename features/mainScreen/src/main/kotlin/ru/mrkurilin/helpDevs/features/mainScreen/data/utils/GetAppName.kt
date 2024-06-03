package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class GetAppName @Inject constructor(
    @ApplicationContext
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
