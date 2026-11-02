package com.example.stylesync.ui.theme.closet

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.stylesync.R
import com.example.stylesync.ui.components.EmptyState
import com.example.stylesync.ui.theme.components.CategoryFilterChips
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
        if (garments.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Checkroom,
                title = stringResource(R.string.closet_empty_title),
                message = stringResource(R.string.closet_empty_message)
            )
        } else {
            GarmentGrid(garments = garments, onGarmentClick = onNavigateToDetail)
        }
    }
}