package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import android.content.Context
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class GetInstalledAppName @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    operator fun invoke(appId: String): String? {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(appId, 0)
            if (applicationInfo.nonLocalizedLabel == null) {
                appId.split(".").last().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            } else {
                applicationInfo.nonLocalizedLabel.toString()
            }
        } catch (e: Exception) {
            null
        }
    }
}
