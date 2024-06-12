package ru.mrkurilin.helpDevs.di

import org.koin.dsl.module
import ru.mrkurilin.helpDevs.features.mainScreen.di.mainScreenModule

val appModule = module {

    includes(mainScreenModule)
}
