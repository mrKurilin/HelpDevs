package ru.mrkurilin.helpDevs.features.mainScreen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
}
