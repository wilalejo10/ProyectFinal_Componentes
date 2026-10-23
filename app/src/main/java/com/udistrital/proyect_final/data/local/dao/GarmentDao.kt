package com.udistrital.proyect_final.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.udistrital.proyect_final.data.local.entity.GarmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GarmentDao {
    @Query("SELECT * FROM garments")
    fun getAllGarments(): Flow<List<GarmentEntity>>

    @Query("SELECT * FROM garments WHERE category = :category")
    fun getGarmentsByCategory(category: String): Flow<List<GarmentEntity>>

    @Query("SELECT * FROM garments WHERE id = :id LIMIT 1")
    suspend fun getGarmentById(id: Int): GarmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarment(garment: GarmentEntity)

    @Update
    suspend fun updateGarment(garment: GarmentEntity)

    @Delete
    suspend fun deleteGarment(garment: GarmentEntity)
}