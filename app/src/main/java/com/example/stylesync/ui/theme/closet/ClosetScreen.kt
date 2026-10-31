package com.example.stylesync.ui.theme.closet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.stylesync.ui.theme.components.CategoryFilterChips
import com.example.stylesync.ui.theme.components.GarmentCard
import com.example.stylesync.ui.theme.components.GarmentGrid

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