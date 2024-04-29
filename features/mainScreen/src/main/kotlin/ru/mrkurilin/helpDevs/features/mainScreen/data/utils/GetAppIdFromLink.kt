package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes
import javax.inject.Inject

class GetAppIdFromLink @Inject constructor() {

    operator fun invoke(appLink: String): String? {
        validAppLinkPrefixes.forEach { validAppLinkPrefix ->
            if (appLink.startsWith(validAppLinkPrefix)) {
                return appLink.removePrefix(validAppLinkPrefix)
            }
        }

        return null
    }
}
