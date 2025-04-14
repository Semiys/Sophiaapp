package com.example.sophiaapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sophiaapp.data.local.preferences.ProfileDao
import com.example.sophiaapp.data.local.preferences.ProfileEntity
import com.example.sophiaapp.data.local.preferences.UserProgressDao
import com.example.sophiaapp.data.local.preferences.UserProgressEntity

@Database(
    entities = [
        ProfileEntity::class,
        UserProgressEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sophia_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}