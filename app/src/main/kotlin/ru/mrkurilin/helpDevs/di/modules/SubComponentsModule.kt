package ru.mrkurilin.helpDevs.di.modules

import dagger.Module
import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenComponent

@Module(
    subcomponents = [
        MainScreenComponent::class,
    ]
)
class SubComponentsModule
