package com.example.stylesync.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.stylesync.data.local.entity.GarmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GarmentDao {
    @Query("SELECT * FROM garments WHERE deleted = 0")
    fun getAllGarments(): Flow<List<GarmentEntity>>

    @Query("SELECT * FROM garments WHERE category = :category AND deleted = 0")
    fun getGarmentsByCategory(category: String): Flow<List<GarmentEntity>>

    @Query("SELECT * FROM garments WHERE id = :id LIMIT 1")
    suspend fun getGarmentById(id: Int): GarmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarment(garment: GarmentEntity)

    @Update
    suspend fun updateGarment(garment: GarmentEntity)

    @Delete
    suspend fun deleteGarment(garment: GarmentEntity)

    // --- nuevas, para sincronizar y para los reportes ---
    @Query("SELECT * FROM garments WHERE deleted = 0")
    suspend fun getAllOnce(): List<GarmentEntity>

    @Query("SELECT * FROM garments WHERE pendingSync = 1")
    suspend fun getPending(): List<GarmentEntity>

    @Query("SELECT * FROM garments WHERE pendingSync = 0")
    suspend fun getSynced(): List<GarmentEntity>

    @Query("SELECT * FROM garments WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getByRemoteId(remoteId: String): GarmentEntity?

    @Query("DELETE FROM garments WHERE id = :id")
    suspend fun hardDelete(id: Int)
}