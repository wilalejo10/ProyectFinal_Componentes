package com.example.stylesync.ui.theme.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.R
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.repository.GarmentRepository
import com.example.stylesync.domain.color.PaletteColorExtractor
import kotlinx.coroutines.launch

class AddGarmentViewModel(private val repo: GarmentRepository) : ViewModel() {
    var name by mutableStateOf("")
    var category by mutableStateOf("Top")
    var colorHex by mutableStateOf("#000000")
    var imageUri by mutableStateOf("")
    var errorRes by mutableStateOf<Int?>(null)

    private var editing: GarmentEntity? = null

    /** Carga la prenda cuando se abre el formulario en modo edicion (id > 0). */
    fun load(id: Int) {
        if (id <= 0 || editing?.id == id) return
        viewModelScope.launch {
            repo.getById(id)?.let {
                editing = it
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
            val base = editing

            val photoIsNew = imageUri.isNotBlank() && imageUri != base?.imageUri
            val finalColor = if (photoIsNew) {
                PaletteColorExtractor.dominantHex(imageUri) ?: colorHex
            } else {
                colorHex
            }

            val garment = if (base != null) {
                base.copy(
                    name = name.trim(),
                    category = category,
                    colorHex = finalColor,
                    imageUri = imageUri,
                    imageUrl = if (imageUri != base.imageUri) "" else base.imageUrl
                )
            } else {
                GarmentEntity(
                    name = name.trim(),
                    category = category,
                    colorHex = finalColor,
                    imageUri = imageUri
                )
            }
            repo.save(garment)
            onSuccess()
        }
    }
}