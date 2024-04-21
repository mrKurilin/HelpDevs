package ru.mrkurilin.helpDevs.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppDatabase
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao

@Module
class DataModule {

    @Provides
    fun provideDataBase(
        @ApplicationContext
        applicationContext: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun provideAppsDao(appDatabase: AppDatabase): AppsDao {
        return appDatabase.appsDao()
    }
}
