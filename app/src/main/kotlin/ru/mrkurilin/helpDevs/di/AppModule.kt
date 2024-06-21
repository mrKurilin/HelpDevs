package ru.mrkurilin.helpDevs.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.ksp.generated.module
import ru.mrkurilin.helpDevs.MainViewModel
import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenModule

val appModule = module {

    includes(
        dataModule,
        MainScreenModule().module,
    )

    viewModelOf(::MainViewModel)
}
