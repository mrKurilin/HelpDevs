package ru.mrkurilin.helpDevs

import android.app.Application
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
}
