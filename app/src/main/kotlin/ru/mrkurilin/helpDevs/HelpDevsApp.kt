package ru.mrkurilin.helpDevs

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mrkurilin.helpDevs.di.appModule

class HelpDevsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HelpDevsApp)
            modules(appModule)
        }
    }
}
