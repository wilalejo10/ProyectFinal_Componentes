package com.example.stylesync.data.model

import com.example.stylesync.data.local.entity.GarmentEntity
data class Outfit(
    val id: String,
    val garments: List<GarmentEntity>,
    val paletteHex: List<String>
)