package com.example.stylesync.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity

// Room 3 necesita KSP (room3-compiler) y un SQLiteDriver explícito.
@Database(entities = [GarmentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun garmentDao(): GarmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                    context = context.applicationContext,
                    name = "outfit_database"
                )
                    .setDriver(BundledSQLiteDriver())
                    .build()
                    .also { INSTANCE = it }
            }
    }
}