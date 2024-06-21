package ru.mrkurilin.helpDevs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import kotlinx.coroutines.launch
import ru.mrkurilin.helpDevs.features.mainScreen.data.AppsRepository
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.IsAppLinkValid

class MainViewModel(
    private val appsRepository: AppsRepository,
    private val isAppLinkValid: IsAppLinkValid,
) : ViewModel() {

    private var bot: Bot? = null

    fun startPolling() {
        val botToken = BuildConfig.TLG_BOT_TOKEN

        viewModelScope.launch {
            if (bot != null || botToken.isEmpty()) {
                return@launch
            }

            bot = bot {
                token = botToken
                dispatch {
                    text {
                        handleReceivedText(message.text?.replace("\n", " ").toString())
                    }
                }
            }

            bot?.startPolling()
        }
    }

    fun stopPolling() {
        if (bot != null) {
            bot?.stopPolling()
            bot = null
        }
    }

    private suspend fun handleReceivedText(text: String) {
        text.split(" ").filter { isAppLinkValid(it) }.forEach { appLink ->
            appsRepository.addApp(appLink.trim())
        }
    }
}
