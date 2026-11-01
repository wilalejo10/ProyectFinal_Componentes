package com.example.stylesync.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [GarmentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun garmentDao(): GarmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "outfit_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override suspend fun onCreate(connection: SQLiteConnection) {
            super.onCreate(connection)

            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.garmentDao()
                    // Datos temporales para Entrega 1
                    dao.insertGarment(GarmentEntity(name = "Camiseta Básica", category = "Top", colorHex = "#FFFFFF", imageUri = ""))
                    dao.insertGarment(GarmentEntity(name = "Jeans Azules", category = "Bottom", colorHex = "#0000FF", imageUri = ""))
                    dao.insertGarment(GarmentEntity(name = "Tenis Blancos", category = "Calzado", colorHex = "#FFFFFF", imageUri = ""))
                }
            }
        }
    }
}