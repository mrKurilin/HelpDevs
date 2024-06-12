package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes

class IsAppLinkValid {

    operator fun invoke(appLink: String): Boolean {
        return validAppLinkPrefixes.any { validAppLinkPrefix ->
            appLink.startsWith(validAppLinkPrefix)
        }
    }
}
