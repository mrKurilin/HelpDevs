package ru.mrkurilin.helpDevs.features.mainScreen.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.koin.core.annotation.Single
import ru.mrkurilin.helpDevs.features.mainScreen.data.VALID_GOOGLE_PLAY_LINK_PREFIX
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.getTextValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val GOOGLE_SHEETS_LINK_1 =
    "https://docs.google.com/spreadsheets/d/17hPaWcE07hKxRRadfUU-R5dz-LBp8JQV9Mpct7J1-uw"

val GOOGLE_SHEETS = listOf(
    GOOGLE_SHEETS_LINK_1,
    "https://docs.google.com/spreadsheets/d/1tfd7bTi9oFo3r3PR8Gomw9lHlD_NYxUsVvMO3X_mQCo",
    "https://docs.google.com/spreadsheets/d/1UChAxIBu1v4lFtsy2Hl7jeNn_ylGWOBRv9D39QuLyMA",
    "https://docs.google.com/spreadsheets/d/1zp5lqAxqY7rMnRliECw3uqdA55MEoVvfGfY07lx57zI",
)

@Single
class AppsFetcher(
    private val getAppIdFromLink: GetAppIdFromLink,
) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    suspend fun fetchApps(): List<AppModel> = withContext(Dispatchers.IO) {
        val remoteApps = mutableListOf<AppModel>()

        val appModelsCanBeInstalled = async {
            getAppModelsCanBeInstalled()
        }

        val appModelsCanBeDeletedAsynced = GOOGLE_SHEETS.map { googleSheetLink ->
            async {
                getAppModelsCanBeDeleted(googleSheetLink)
            }
        }

        remoteApps.addAll(appModelsCanBeInstalled.await())

        appModelsCanBeDeletedAsynced.forEach { deferredList ->
            remoteApps.addAll(deferredList.await())
        }

        return@withContext remoteApps
    }

    private fun getAppModelsCanBeInstalled(): List<AppModel> {
        val appModels = mutableListOf<AppModel>()
        val currentDate = Date()
        val rows = Jsoup.connect(GOOGLE_SHEETS_LINK_1).get().body().select("tr")

        rows.forEach { row ->
            val columns = row.select("td")

            if (columns.size < 6) {
                return@forEach
            }

            val date = columns[5].getTextValue()
            val canBeDeletedAlready = try {
                Date(dateFormat.parse(date)!!.time + 24 * 60 * 60 * 1000) < currentDate
            } catch (e: Exception) {
                false
            }

            createAppModel(
                appName = columns[3].getTextValue(),
                appLink = columns[4].getTextValue(),
                canBeDeleted = canBeDeletedAlready,
            )?.let { appModel ->
                appModels.add(appModel)
            }
        }

        return appModels
    }

    private fun getAppModelsCanBeDeleted(url: String): List<AppModel> {
        val appModels = mutableListOf<AppModel>()
        val rows = Jsoup.connect(url).get().body().select("tr")

        rows.forEach { row ->
            val columns = row.select("td")

            if (columns.size < 2) {
                return@forEach
            }

            createAppModel(
                appName = columns[0].getTextValue(),
                appLink = columns[1].getTextValue(),
                canBeDeleted = true,
            )?.let { appModel ->
                appModels.add(appModel)
            }
        }

        return appModels
    }

    private fun createAppModel(
        appName: String,
        appLink: String,
        canBeDeleted: Boolean,
    ): AppModel? {
        val appId = getAppIdFromLink(appLink) ?: return null

        val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId

        return AppModel(
            appName = appName,
            appLink = validAppLink.split("&").first(),
            appId = appId,
            canBeDeleted = canBeDeleted,
        )
    }
}
