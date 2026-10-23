package com.udistrital.proyect_final.ui.theme.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.udistrital.proyect_final.ui.theme.components.ColorChip
import com.udistrital.proyect_final.ui.theme.components.ConfirmDialog

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