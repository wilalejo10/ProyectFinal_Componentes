package com.example.stylesync.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "garments")
data class GarmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // ej. "Top", "Bottom", "Calzado", "Accesorio"
    val colorHex: String,
    val imageUri: String, // ruta local de la foto
    // --- campos de sincronizacion ---
    val remoteId: String = UUID.randomUUID().toString(), // id del documento en Firestore
    val imageUrl: String = "",                           // URL de la foto en Storage
    val updatedAt: Long = System.currentTimeMillis(),
    val pendingSync: Boolean = true,
    val deleted: Boolean = false                         // borrado logico hasta sincronizar
)