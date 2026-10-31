package com.example.stylesync.ui.theme.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GarmentDetailViewModel(private val dao: GarmentDao) : ViewModel() {
    private val _garment = MutableStateFlow<GarmentEntity?>(null)
    val garment: StateFlow<GarmentEntity?> = _garment

    fun loadGarment(id: Int) {
        viewModelScope.launch {
            _garment.value = dao.getGarmentById(id)
        }
    }

    fun deleteGarment(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _garment.value?.let {
                dao.deleteGarment(it)
                onDeleted()
            }
        }
    }
}