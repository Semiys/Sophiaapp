package com.example.sophiaapp.data.local.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object для работы с таблицей прогресса пользователя
 */
@Dao
interface UserProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(progressEntity: UserProgressEntity)

    @Query("SELECT * FROM user_progress WHERE userId = :userId LIMIT 1")
    fun getUserProgressFlow(userId: String): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE userId = :userId LIMIT 1")
    suspend fun getUserProgress(userId: String): UserProgressEntity?

    @Query("DELETE FROM user_progress WHERE userId = :userId")
    suspend fun deleteUserProgress(userId: String)

    @Query("SELECT * FROM user_progress WHERE userId = :userId LIMIT 1")
    fun getUserProgressSync(userId: String): UserProgressEntity?
}