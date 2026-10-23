package com.udistrital.proyect_final.ui.theme.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.udistrital.proyect_final.ui.theme.components.CategoryDropdown

@Composable
fun AddEditGarmentScreen(viewModel: AddGarmentViewModel, onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Nombre de la prenda") },
            isError = viewModel.errorText != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (viewModel.errorText != null) {
            Text(text = viewModel.errorText!!, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))


        CategoryDropdown(
            selectedCategory = viewModel.category,
            onCategorySelected = { viewModel.category = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.saveGarment(onSuccess = onNavigateBack) }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Prenda")
        }
    }
}