package ru.mrkurilin.helpDevs.features.mainScreen.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.VALID_GOOGLE_PLAY_LINK_PREFIX
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.getTextValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

const val GOOGLE_SHEETS_LINK_1 =
    "https://docs.google.com/spreadsheets/d/17hPaWcE07hKxRRadfUU-R5dz-LBp8JQV9Mpct7J1-uw"
const val GOOGLE_SHEETS_LINK_2 =
    "https://docs.google.com/spreadsheets/d/1tfd7bTi9oFo3r3PR8Gomw9lHlD_NYxUsVvMO3X_mQCo"
const val GOOGLE_SHEETS_LINK_3 =
    "https://docs.google.com/spreadsheets/d/1UChAxIBu1v4lFtsy2Hl7jeNn_ylGWOBRv9D39QuLyMA"

@AppScope
class AppsFetcher @Inject constructor(
    private val getAppIdFromLink: GetAppIdFromLink,
) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    suspend fun fetchApps(): List<AppModel> {
        return with(CoroutineScope(coroutineContext)) {
            val appModelsCanBeInstalled = async {
                getAppModelsCanBeInstalled()
            }
            val appModelsCanBeDeleted1 = async {
                getAppModelsCanBeDeleted(GOOGLE_SHEETS_LINK_1)
            }
            val appModelsCanBeDeleted2 = async {
                getAppModelsCanBeDeleted(GOOGLE_SHEETS_LINK_2)
            }
            val appModelsCanBeDeleted3 = async {
                getAppModelsCanBeDeleted(GOOGLE_SHEETS_LINK_3)
            }

            mutableListOf<AppModel>().apply {
                addAll(appModelsCanBeInstalled.await())
                addAll(appModelsCanBeDeleted1.await())
                addAll(appModelsCanBeDeleted2.await())
                addAll(appModelsCanBeDeleted3.await())
            }
        }
    }

    private fun getAppModelsCanBeInstalled(): List<AppModel> {
        val appModels = mutableListOf<AppModel>()
        val currentDate = Date()
        val rows = Jsoup.connect(GOOGLE_SHEETS_LINK_1).get().body().select("tr")

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

    private fun getAppModelsCanBeDeleted(url: String): List<AppModel> {
        val appModels = mutableListOf<AppModel>()
        val rows = Jsoup.connect(url).get().body().select("tr")

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

    private fun getAppModel(
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
