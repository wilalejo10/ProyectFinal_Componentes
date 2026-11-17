package com.example.stylesync.ui.theme.closet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.EmptyState
import com.example.stylesync.ui.components.ErrorMessage
import com.example.stylesync.ui.theme.components.CategoryFilterChips
import com.example.stylesync.ui.theme.components.GarmentGrid

@Composable
fun ClosetScreen(viewModel: ClosetViewModel, onNavigateToDetail: (Int) -> Unit) {
    val garments by viewModel.garments.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val loaded by viewModel.loaded.collectAsState()
    val syncing by viewModel.syncing.collectAsState()
    val syncFailed by viewModel.syncFailed.collectAsState()

    Column {
        CategoryFilterChips(
            categories = viewModel.categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.setCategoryFilter(it) }
        )

        // Barra fina mientras se sincroniza con Firebase
        if (syncing) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        // Error de sincronizacion (por ejemplo sin internet): la app sigue funcionando en local
        if (syncFailed && !syncing) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ErrorMessage(
                    text = stringResource(R.string.closet_sync_error),
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = viewModel::refresh) {
                    Text(stringResource(R.string.closet_retry))
                }
            }
        }

        when {
            // Cargando: Room todavia no entrego la primera lista
            !loaded -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            // Vacio: distinto mensaje si hay un filtro activo
            garments.isEmpty() && selectedCategory != null -> EmptyState(
                icon = Icons.Filled.Checkroom,
                title = stringResource(R.string.closet_filter_empty_title),
                message = stringResource(R.string.closet_filter_empty_message)
            )
            garments.isEmpty() -> EmptyState(
                icon = Icons.Filled.Checkroom,
                title = stringResource(R.string.closet_empty_title),
                message = stringResource(R.string.closet_empty_message)
            )
            else -> GarmentGrid(garments = garments, onGarmentClick = onNavigateToDetail)
        }
    }
}