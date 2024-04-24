package ru.mrkurilin.helpDevs.features.mainScreen.data.remote

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.GOOGLE_SHEETS_LINK_1
import ru.mrkurilin.helpDevs.features.mainScreen.data.GOOGLE_SHEETS_LINK_2
import ru.mrkurilin.helpDevs.features.mainScreen.data.GOOGLE_SHEETS_LINK_3
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.getTextValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val VALID_GOOGLE_PLAY_LINK_PREFIX = "https://play.google.com/store/apps/details?id="

private val validAppLinkPrefixes = listOf(
    VALID_GOOGLE_PLAY_LINK_PREFIX,
    "https://play.google.com/apps/testing/"
)

@AppScope
class AppsFetcher @Inject constructor() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun fetchApps(): List<AppModel> {
        val rows1 = Jsoup.connect(GOOGLE_SHEETS_LINK_1).get().body().select("tr")

        val appModelsCanBeInstalled = getAppModelsCanBeInstalled(rows1)
        val appModelsCanBeDeleted1 = getAppModelsCanBeDeleted(rows1)

        val rows2 = Jsoup.connect(GOOGLE_SHEETS_LINK_2).get().body().select("tr")
        val appModelsCanBeDeleted2 = getAppModelsCanBeDeleted(rows2)

        val rows3 = Jsoup.connect(GOOGLE_SHEETS_LINK_3).get().body().select("tr")
        val appModelsCanBeDeleted3 = getAppModelsCanBeDeleted(rows3)

        return appModelsCanBeInstalled + appModelsCanBeDeleted1 + appModelsCanBeDeleted2 + appModelsCanBeDeleted3
    }

    private fun getAppModelsCanBeInstalled(rows: Elements): List<AppModel> {
        val appModels = mutableListOf<AppModel>()
        val currentDate = Date()

        rows.forEach { row ->
            val columns = row.select("td")

            if (columns.size < 7) {
                return@forEach
            }

            val date = columns[6].getTextValue()
            val canBeDeletedAlready = try {
                Date(dateFormat.parse(date)!!.time + 24 * 60 * 60 * 1000) < currentDate
            } catch (e: Exception) {
                false
            }

            getAppModel(
                appName = columns[3].getTextValue(),
                appLink = columns[4].getTextValue(),
                canBeDeleted = canBeDeletedAlready,
            )?.let { appModel ->
                appModels.add(appModel)
            }
        }

        return appModels
    }

    private fun getAppModel(
        appName: String,
        appLink: String,
        canBeDeleted: Boolean,
    ): AppModel? {
        if (validAppLinkPrefixes.none { validAppLinkPrefix ->
                appLink.startsWith(validAppLinkPrefix)
            }) {
            return null
        }

        val appId = getAppId(appLink)
        val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId

        return AppModel(
            appName = appName,
            appLink = validAppLink,
            appId = appId,
            canBeDeleted = canBeDeleted,
        )
    }

    private fun getAppId(appLink: String): String {
        validAppLinkPrefixes.forEach { validAppLinkPrefix ->
            if (appLink.startsWith(validAppLinkPrefix)) {
                return appLink.removePrefix(validAppLinkPrefix)
            }
        }

        return ""
    }

    private fun getAppModelsCanBeDeleted(rows: Elements): List<AppModel> {
        val appModels = mutableListOf<AppModel>()

        rows.forEach { row ->
            val columns = row.select("td")

            if (columns.size < 2) {
                return@forEach
            }

            getAppModel(
                appName = columns[0].getTextValue(),
                appLink = columns[1].getTextValue(),
                canBeDeleted = true,
            )?.let { appModel ->
                appModels.add(appModel)
            }
        }

        return appModels
    }
}
