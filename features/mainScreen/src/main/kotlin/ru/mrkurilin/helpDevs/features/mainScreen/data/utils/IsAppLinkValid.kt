package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes
import javax.inject.Inject

class IsAppLinkValid @Inject constructor() {

    operator fun invoke(appLink: String): Boolean {
        return validAppLinkPrefixes.any { validAppLinkPrefix ->
            appLink.startsWith(validAppLinkPrefix)
        }
    }
}
