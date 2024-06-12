package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import org.koin.core.annotation.Single
import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes

@Single
class GetAppIdFromLink {

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
