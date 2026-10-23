package com.udistrital.proyect_final.ui.theme.closet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.proyect_final.data.local.dao.GarmentDao
import com.udistrital.proyect_final.data.local.entity.GarmentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClosetViewModel(private val dao: GarmentDao) : ViewModel() {
    private val _garments = MutableStateFlow<List<GarmentEntity>>(emptyList())
    val garments: StateFlow<List<GarmentEntity>> = _garments

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val categories = listOf("Top", "Bottom", "Calzado", "Accesorio")

    init { loadGarments() }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
        loadGarments()
    }

    private fun loadGarments() {
        viewModelScope.launch {
            val category = _selectedCategory.value
            val flow = if (category == null) dao.getAllGarments() else dao.getGarmentsByCategory(category)
            flow.collect { _garments.value = it }
        }
    }
}