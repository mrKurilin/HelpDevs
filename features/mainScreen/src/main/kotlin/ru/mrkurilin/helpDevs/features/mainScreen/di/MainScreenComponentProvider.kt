package ru.mrkurilin.helpDevs.features.mainScreen.di

import ru.mrkurilin.helpDevs.di.api.SubComponentsProvider

interface MainScreenComponentProvider : SubComponentsProvider {

    fun provideMainScreenComponent(): MainScreenComponent
}
