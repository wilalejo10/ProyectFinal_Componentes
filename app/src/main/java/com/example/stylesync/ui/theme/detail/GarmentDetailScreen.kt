package com.example.stylesync.ui.theme.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.EmptyState
import com.example.stylesync.ui.components.GarmentImage
import com.example.stylesync.ui.theme.components.ColorChip
import com.example.stylesync.ui.theme.components.ConfirmDialog

@Composable
fun GarmentDetailScreen(
    viewModel: GarmentDetailViewModel,
    garmentId: Int,
    onNavigateUp: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val garment by viewModel.garment.collectAsState()
    val notFound by viewModel.notFound.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(garmentId) {
        viewModel.loadGarment(garmentId)
    }

    val item = garment
    when {
        // ---- Prenda cargada ----
        item != null -> {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                GarmentImage(
                    imageUri = item.imageUri,
                    imageUrl = item.imageUrl,
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = stringResource(R.string.detail_category, item.category),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.detail_color) + " ",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    ColorChip(colorHex = item.colorHex)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onNavigateToEdit(item.id) }) {
                        Text(stringResource(R.string.detail_edit))
                    }
                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.detail_delete))
                    }
                }
            }

            ConfirmDialog(
                showDialog = showDeleteDialog,
                title = stringResource(R.string.detail_delete_title),
                message = stringResource(R.string.detail_delete_message),
                onConfirm = {
                    showDeleteDialog = false
                    viewModel.deleteGarment(onDeleted = onNavigateUp)
                },
                onDismiss = { showDeleteDialog = false }
            )
        }

        // ---- Error: la prenda no existe ----
        notFound -> EmptyState(
            icon = Icons.Filled.Checkroom,
            title = stringResource(R.string.detail_not_found_title),
            message = stringResource(R.string.detail_not_found_message),
            actionText = stringResource(R.string.detail_back),
            onAction = onNavigateUp
        )

        // ---- Cargando ----
        else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}