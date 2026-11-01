package com.example.stylesync.data.model

import com.example.stylesync.data.local.entity.GarmentEntity

object SampleData {
    val garments = listOf(
        GarmentEntity(id = 1, name = "Camiseta blanca", category = "Top", colorHex = "#F5F5F5", imageUri = ""),
        GarmentEntity(id = 2, name = "Jeans azules", category = "Bottom", colorHex = "#2F4F7F", imageUri = ""),
        GarmentEntity(id = 3, name = "Tenis negros", category = "Calzado", colorHex = "#1A1A1A", imageUri = ""),
        GarmentEntity(id = 4, name = "Chaqueta beige", category = "Accesorio", colorHex = "#D2B48C", imageUri = "")
    )

    val outfit = Outfit(
        id = "o1",
        garments = garments.take(3),
        paletteHex = garments.take(3).map { it.colorHex }
    )
}