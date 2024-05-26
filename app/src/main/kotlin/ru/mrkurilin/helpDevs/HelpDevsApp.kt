package ru.mrkurilin.helpDevs

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import ru.mrkurilin.helpDevs.di.AppComponent
import ru.mrkurilin.helpDevs.di.DaggerAppComponent
import ru.mrkurilin.helpDevs.di.SubComponentsProviderImpl
import ru.mrkurilin.helpDevs.di.api.SubComponentsProvider
import ru.mrkurilin.helpDevs.di.api.SubComponentsProviderHolder

class HelpDevsApp : Application(), SubComponentsProviderHolder {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override val subComponentsProvider: SubComponentsProvider by lazy {
        SubComponentsProviderImpl(appComponent)
    }

    override fun onCreate() {
        super.onCreate()
        val config = AppMetricaConfig.newConfigBuilder(API_KEY).build()
        AppMetrica.activate(this, config)
    }
}
