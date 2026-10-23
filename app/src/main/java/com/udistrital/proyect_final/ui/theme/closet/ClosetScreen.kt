package com.udistrital.proyect_final.ui.theme.closet

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.udistrital.proyect_final.ui.theme.components.CategoryFilterChips
import com.udistrital.proyect_final.ui.theme.components.GarmentGrid


@Composable
fun ClosetScreen(viewModel: ClosetViewModel, onNavigateToDetail: (Int) -> Unit) {
    val garments by viewModel.garments.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column {
        CategoryFilterChips(
            categories = viewModel.categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.setCategoryFilter(it) }
        )
        GarmentGrid(
            garments = garments,
            onGarmentClick = onNavigateToDetail
        )
    }
}