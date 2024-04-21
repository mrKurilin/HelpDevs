package ru.mrkurilin.helpDevs.features.mainScreen.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppModel(
    @PrimaryKey val appId: String,
    val appName: String,
    val appLink: String,
    val appearanceDate: Long,
    val canBeDeleted: Boolean,
    val installDate: Long? = null,
    val isInstalled: Boolean = false,
)
