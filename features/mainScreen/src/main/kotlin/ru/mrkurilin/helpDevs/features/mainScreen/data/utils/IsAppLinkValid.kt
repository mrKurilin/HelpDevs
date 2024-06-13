package ru.mrkurilin.helpDevs.features.mainScreen.data.utils

import org.koin.core.annotation.Single
import ru.mrkurilin.helpDevs.features.mainScreen.data.validAppLinkPrefixes

@Single
class IsAppLinkValid {

    operator fun invoke(appLink: String): Boolean {
        return validAppLinkPrefixes.any { validAppLinkPrefix ->
            appLink.startsWith(validAppLinkPrefix)
        }
    }
}
