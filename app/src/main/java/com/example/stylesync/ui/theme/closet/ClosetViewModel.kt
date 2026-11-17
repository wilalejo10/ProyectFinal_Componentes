package com.example.stylesync.ui.theme.closet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.repository.GarmentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClosetViewModel(private val repo: GarmentRepository) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val categories = listOf("Top", "Bottom", "Calzado", "Accesorio")

    // false hasta que Room entrega la primera lista: sirve para mostrar "cargando" en vez de "vacio"
    private val _loaded = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded

    private val _syncing = MutableStateFlow(false)
    val syncing: StateFlow<Boolean> = _syncing

    // true si la ultima sincronizacion con Firebase fallo (por ejemplo sin internet)
    private val _syncFailed = MutableStateFlow(false)
    val syncFailed: StateFlow<Boolean> = _syncFailed

    @OptIn(ExperimentalCoroutinesApi::class)
    val garments: StateFlow<List<GarmentEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == null) repo.garments else repo.garmentsByCategory(category)
        }
        .onEach { _loaded.value = true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        refresh()
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }

    /** Sube lo pendiente y baja lo nuevo de Firestore. */
    fun refresh() {
        viewModelScope.launch {
            _syncing.value = true
            _syncFailed.value = !repo.sync()
            _syncing.value = false
        }
    }
}