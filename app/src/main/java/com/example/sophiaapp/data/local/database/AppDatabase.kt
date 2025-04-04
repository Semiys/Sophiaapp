package com.example.sophiaapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sophiaapp.data.local.preferences.ProfileDao
import com.example.sophiaapp.data.local.preferences.ProfileEntity

@Database(entities=[ProfileEntity::class],version=1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getDatabase(context:Context):AppDatabase{
            return INSTANCE?:synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sophia_database"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }
}