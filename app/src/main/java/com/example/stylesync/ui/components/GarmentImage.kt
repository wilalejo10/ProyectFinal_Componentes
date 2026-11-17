package com.example.stylesync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * Foto de la prenda. Usa la ruta local si existe; si no (prenda bajada de Firestore),
 * usa la URL de Storage. Sin ninguna de las dos, muestra un recuadro con un icono.
 */
@Composable
fun GarmentImage(
    imageUri: String,
    modifier: Modifier = Modifier,
    imageUrl: String = ""
) {
    val model = imageUri.ifBlank { imageUrl }
    if (model.isBlank()) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Checkroom,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        AsyncImage(
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}