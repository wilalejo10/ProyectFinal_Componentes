package com.udistrital.proyect_final.data.local.entity

import androidx.room.PrimaryKey
import androidx.room3.Entity

@Entity(tableName = "garments")
data class GarmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val colorHex: String,
    val imageUri: String
)