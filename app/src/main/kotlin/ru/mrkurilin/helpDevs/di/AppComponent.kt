package ru.mrkurilin.helpDevs.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.mrkurilin.helpDevs.di.modules.DataModule
import ru.mrkurilin.helpDevs.di.modules.NetworkModule
import ru.mrkurilin.helpDevs.di.modules.SubComponentsModule
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import ru.mrkurilin.helpDevs.features.mainScreen.di.MainScreenComponent

@AppScope
@Component(
    modules = [
        SubComponentsModule::class,
        NetworkModule::class,
        DataModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            @ApplicationContext
            context: Context,
        ): AppComponent
    }

    fun mainScreenComponentFactory(): MainScreenComponent.Factory
}
