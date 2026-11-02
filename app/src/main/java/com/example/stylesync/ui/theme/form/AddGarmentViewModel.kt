package com.example.stylesync.ui.theme.form

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.R
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity
import kotlinx.coroutines.launch

class AddGarmentViewModel(private val dao: GarmentDao) : ViewModel() {
    var name by mutableStateOf("")
    var category by mutableStateOf("Top")
    var colorHex by mutableStateOf("#000000")
    var imageUri by mutableStateOf("")
    @StringRes var errorRes by mutableStateOf<Int?>(null)

    private var editingId = 0

    /** Carga la prenda cuando se abre el formulario en modo edición (id > 0). */
    fun load(id: Int) {
        if (id <= 0 || editingId == id) return
        viewModelScope.launch {
            dao.getGarmentById(id)?.let {
                editingId = it.id
                name = it.name
                category = it.category
                colorHex = it.colorHex
                imageUri = it.imageUri
            }
        }
    }

    fun saveGarment(onSuccess: () -> Unit) {
        if (name.isBlank()) {
            errorRes = R.string.garment_error_name
            return
        }
        viewModelScope.launch {
            val garment = GarmentEntity(
                id = editingId, // 0 = nueva (autogenera id)
                name = name.trim(),
                category = category,
                colorHex = colorHex,
                imageUri = imageUri
            )
            if (editingId > 0) dao.updateGarment(garment) else dao.insertGarment(garment)
            onSuccess()
        }
    }
}