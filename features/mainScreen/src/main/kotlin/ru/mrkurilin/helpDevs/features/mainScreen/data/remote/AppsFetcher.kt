package ru.mrkurilin.helpDevs.features.mainScreen.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val GOOGLE_SHEETS_LINK = "https://docs.google.com/spreadsheets/d/1mWzPRzr_H480l3s_U8gp7Imh6AuTkHYCMGxNB9qC5EI"

private const val VALID_GOOGLE_PLAY_LINK_PREFIX = "https://play.google.com/store/apps/details?id="

private val validAppLinkPrefixes = listOf(
    VALID_GOOGLE_PLAY_LINK_PREFIX,
    "https://play.google.com/apps/testing/"
)

@AppScope
class AppsFetcher @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {

    fun fetchApps(): List<AppModel> {
        val currentDate = Date()
        val doc = Jsoup.parse(getTablesHtml())
        val tables = doc.select("table")

        if (tables.size < 2) {
            return emptyList()
        }

        return getAppModelsCanBeInstalled(
            rows = tables[0].select("tbody").select("tr"),
            currentDate = currentDate,
        ) + getAppModelsCanBeDeleted(
            rows = tables[1].select("tbody").select("tr"),
        )
    }

    private fun getAppModelsCanBeInstalled(
        rows: Elements,
        currentDate: Date,
    ): List<AppModel> {
        val appModels = mutableListOf<AppModel>()

        rows.forEach { row ->
            val cells = row.select("td")

            if (cells.size < 8) {
                return@forEach
            }

            val appName = cells[5].getValue().removePrefix("\n").trim()

            val appLink = cells[6].getValue().trim()
            if (isAppLinkNotValid(appLink)) {
                return@forEach
            }

            val date = cells[7].getValue().trim()
            val canBeDeletedAlready = canBeDeletedAlready(
                dateString = date,
                currentDate = currentDate,
            )

            val appId = getAppId(appLink)
            val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId
            appModels.add(
                AppModel(
                    appName = appName,
                    appLink = validAppLink,
                    appId = appId,
                    canBeDeleted = canBeDeletedAlready,
                    appearanceDate = Date().time
                )
            )
        }

        return appModels
    }

    private fun getAppId(appLink: String): String {
        validAppLinkPrefixes.forEach { validAppLinkPrefix ->
            if (appLink.startsWith(validAppLinkPrefix)) {
                return appLink.removePrefix(validAppLinkPrefix)
            }
        }

        return ""
    }

    private fun getAppModelsCanBeDeleted(
        rows: Elements,
    ): List<AppModel> {
        val appModels = mutableListOf<AppModel>()

        rows.forEach { row ->
            val cells = row.select("td")

            if (cells.size < 4) {
                return@forEach
            }

            val appName = cells[2].getValue().removePrefix("\n").trim()
            val appLink = cells[3].getValue().trim()

            if (isAppLinkNotValid(appLink)) {
                return@forEach
            }

            val appId = getAppId(appLink)
            val validAppLink = VALID_GOOGLE_PLAY_LINK_PREFIX + appId
            appModels.add(
                AppModel(
                    appName = appName,
                    appLink = validAppLink,
                    appId = appId,
                    canBeDeleted = true,
                    appearanceDate = Date().time
                )
            )
        }

        return appModels
    }

    private fun getTablesHtml(): String {
        val request = Request.Builder()
            .url(GOOGLE_SHEETS_LINK)
            .build()

        return try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                response.body()?.string() ?: ""
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun Element.getValue(): String {
        val childNodes: List<org.jsoup.nodes.Node> = childNodes()

        if (childNodes.isEmpty()) {
            return ""
        }

        val firstChildNote = childNodes.first()

        if (firstChildNote is org.jsoup.nodes.TextNode) {
            return firstChildNote.toString()
        }

        if (firstChildNote is Element) {
            return firstChildNote.getValue()
        }

        return ""
    }

    private fun canBeDeletedAlready(
        dateString: String,
        currentDate: Date,
    ): Boolean {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)!! <= currentDate
        } catch (e: Exception) {
            false
        }
    }

    private fun isAppLinkNotValid(appLink: String): Boolean {
        if (appLink.isBlank()) {
            return true
        }

        return validAppLinkPrefixes.none { validAppLinkPrefix ->
            appLink.startsWith(validAppLinkPrefix)
        }
    }
}
