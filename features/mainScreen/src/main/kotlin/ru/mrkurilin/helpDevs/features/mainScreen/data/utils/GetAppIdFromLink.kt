package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes
import javax.inject.Inject

class GetAppIdFromLink @Inject constructor() {

    operator fun invoke(appLink: String): String? {
        val trimmedAppLink = appLink.trim()
        validAppLinkPrefixes.forEach { validAppLinkPrefix ->
            if (trimmedAppLink.startsWith(validAppLinkPrefix)) {
                return trimmedAppLink.removePrefix(validAppLinkPrefix).split("&").first()
            }
        }

        return null
    }
}
