package com.example.stylesync.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "garments")
data class GarmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // ej. "Top", "Bottom", "Calzado", "Accesorio"
    val colorHex: String,
    val imageUri: String // Ruta local o URI falsa para la Entrega 1
)