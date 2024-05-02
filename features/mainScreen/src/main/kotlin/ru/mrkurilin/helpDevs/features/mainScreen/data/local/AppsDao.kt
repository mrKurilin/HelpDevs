package ru.mrkurilin.helpDevs.features.mainScreen.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppsDao {

    @Query("SELECT * FROM appModel ORDER BY appId COLLATE NOCASE ASC")
    fun getAllAppsFlow(): Flow<List<AppModel>>

    @Query("SELECT * FROM appModel")
    fun getAllAppsList(): List<AppModel>

    @Query("SELECT appId FROM appModel")
    fun getAllAppIds(): List<String>

    @Query("SELECT * FROM appModel WHERE appId LIKE :appId")
    suspend fun getAppModelById(appId: String): AppModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(appModel: AppModel)

    @Update
    suspend fun update(appModel: AppModel)
}
