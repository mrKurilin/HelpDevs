package ru.mrkurilin.helpDevs.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppModel
import ru.mrkurilin.helpDevs.features.mainScreen.data.local.AppsDao

@Database(entities = [AppModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
}
