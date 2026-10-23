package com.udistrital.proyect_final.ui.theme.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.udistrital.proyect_final.data.local.entity.GarmentEntity

@Composable
fun GarmentGrid(
    garments: List<GarmentEntity>,
    onGarmentClick: (Int) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(garments) { garment ->
            GarmentCard(
                garmentName = garment.name,
                category = garment.category,
                colorHex = garment.colorHex,
                onClick = { onGarmentClick(garment.id) }
            )
        }
    }
}