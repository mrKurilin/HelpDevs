package ru.mrkurilin.helpDevs.features.mainScreen.di

import dagger.Subcomponent
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.MainScreenViewModel

@Subcomponent()
interface MainScreenComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): MainScreenComponent
    }

    fun mainScreenViewModel(): MainScreenViewModel
}
