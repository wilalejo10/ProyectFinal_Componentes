package com.example.stylesync.ui.theme.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.repository.GarmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GarmentDetailViewModel(private val repository: GarmentRepository) : ViewModel() {
    private val _garment = MutableStateFlow<GarmentEntity?>(null)
    val garment: StateFlow<GarmentEntity?> = _garment

    /** true cuando ya se consulto y la prenda no existe (o esta marcada como borrada). */
    private val _notFound = MutableStateFlow(false)
    val notFound: StateFlow<Boolean> = _notFound

    fun loadGarment(id: Int) {
        viewModelScope.launch {
            val found = repository.getById(id)?.takeIf { !it.deleted }
            _garment.value = found
            _notFound.value = found == null
        }
    }

    fun deleteGarment(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _garment.value?.let {
                repository.delete(it)
                onDeleted()
            }
        }
    }
}