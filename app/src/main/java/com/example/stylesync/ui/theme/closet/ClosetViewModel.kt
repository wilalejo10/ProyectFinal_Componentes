package com.example.stylesync.ui.theme.closet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.local.entity.GarmentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ClosetViewModel(private val dao: GarmentDao) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val categories = listOf("Top", "Bottom", "Calzado", "Accesorio")

    // flatMapLatest cancela el filtro anterior: antes cada cambio de categoría dejaba un collector viejo vivo.
    @OptIn(ExperimentalCoroutinesApi::class)
    val garments: StateFlow<List<GarmentEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == null) dao.getAllGarments() else dao.getGarmentsByCategory(category)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }
}