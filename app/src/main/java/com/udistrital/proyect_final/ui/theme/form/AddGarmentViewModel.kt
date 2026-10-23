package com.udistrital.proyect_final.ui.theme.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.proyect_final.data.local.dao.GarmentDao
import com.udistrital.proyect_final.data.local.entity.GarmentEntity
import kotlinx.coroutines.launch

class AddGarmentViewModel(private val dao: GarmentDao) : ViewModel() {
    var name by mutableStateOf("")
    var category by mutableStateOf("Top")
    var colorHex by mutableStateOf("#000000")
    var errorText by mutableStateOf<String?>(null)

    fun saveGarment(onSuccess: () -> Unit) {
        if (name.isBlank()) {
            errorText = "El nombre no puede estar vacío"
            return
        }
        viewModelScope.launch {
            dao.insertGarment(
                GarmentEntity(
                    name = name,
                    category = category,
                    colorHex = colorHex,
                    imageUri = ""
                )
            )
            onSuccess()
        }
    }
}