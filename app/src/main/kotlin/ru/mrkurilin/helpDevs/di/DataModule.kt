package ru.mrkurilin.helpDevs.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mrkurilin.helpDevs.data.AppDatabase
import ru.mrkurilin.helpDevs.data.migrations.MIGRATION_1_2
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao

val dataModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "database-name"
        ).addMigrations(
            MIGRATION_1_2,
        ).build()
    }

    single<AppsDao> { get<AppDatabase>().appsDao() }
}
