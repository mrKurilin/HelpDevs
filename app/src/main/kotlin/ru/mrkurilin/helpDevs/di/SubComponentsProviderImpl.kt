package ru.mrkurilin.helpDevs.di

import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenComponent
import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenComponentProvider

class SubComponentsProviderImpl(
    private val appComponent: AppComponent
) : MainScreenComponentProvider {

    override fun provideMainScreenComponent(): MainScreenComponent {
        return appComponent.mainScreenComponentFactory().create()
    }
}
