package com.example.stylesync.ui.theme.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(garmentId) {
        viewModel.loadGarment(garmentId)
    }

    garment?.let { item ->
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            // Placeholder para la foto de la prenda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Categoría: ${item.category}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Color: ", style = MaterialTheme.typography.bodyLarge)
                ColorChip(colorHex = item.colorHex)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onNavigateToEdit(item.id) }) {
                    Text("Editar")
                }
                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            }
        }

        ConfirmDialog(
            showDialog = showDeleteDialog,
            title = "Eliminar Prenda",
            message = "¿Estás seguro de que deseas eliminar esta prenda? Esta acción no se puede deshacer.",
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteGarment(onDeleted = onNavigateUp)
            },
            onDismiss = { showDeleteDialog = false }
        )
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}