package ru.mrkurilin.helpDevs.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE appModel ADD COLUMN ownerTlgUserName TEXT DEFAULT NULL")
    }
}
