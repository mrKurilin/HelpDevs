package ru.mrkurilin.helpDevs.features.mainScreen.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mrkurilin.helpDevs.features.mainScreen.data.AppsRepository
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppDatabase
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao
import ru.mrkurilin.helpDevs.features.mainScreen.data.remote.AppsFetcher
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppIdFromLink
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetAppName
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.GetInstalledAppIds
import ru.mrkurilin.helpDevs.features.mainScreen.data.utils.IsAppLinkValid
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.MainScreenViewModel
import ru.mrkurilin.helpDevs.features.mainScreen.presentation.model.AppUiModelMapper

val mainScreenModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "database-name"
        ).build()
    }

    single<AppsDao> { get<AppDatabase>().appsDao() }

    singleOf(::GetAppName)
    singleOf(::GetInstalledAppIds)
    singleOf(::AppUiModelMapper)
    singleOf(::AppsFetcher)
    singleOf(::AppsRepository)
    singleOf(::GetAppIdFromLink)
    singleOf(::IsAppLinkValid)
    viewModelOf(::MainScreenViewModel)
}
