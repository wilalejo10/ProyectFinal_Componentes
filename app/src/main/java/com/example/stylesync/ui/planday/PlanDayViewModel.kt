package com.example.stylesync.ui.planday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.local.dao.GarmentDao
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Outfit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanDayViewModel(private val dao: GarmentDao) : ViewModel() {

    private val _uiState = MutableStateFlow(PlanDayUiState())
    val uiState: StateFlow<PlanDayUiState> = _uiState.asStateFlow()

    init {
        loadGarments()
    }

    private fun loadGarments() {
        viewModelScope.launch {
            dao.getAllGarments().collect { garments ->
                _uiState.update { it.copy(availableGarments = garments) }
            }
        }
    }

    fun onOccasionSelected(occasion: Occasion) {
        _uiState.update { it.copy(occasion = occasion, occasionError = false) }
    }

    fun onGarmentToggled(garmentId: Int) {
        _uiState.update { state ->
            val newSelection =
                if (garmentId in state.selectedGarmentIds) state.selectedGarmentIds - garmentId
                else state.selectedGarmentIds + garmentId
            state.copy(selectedGarmentIds = newSelection, garmentsError = false)
        }
    }

    fun onGenerateClick(): Outfit? {
        val state = _uiState.value
        val occasionMissing = state.occasion == null
        val garmentsMissing = state.selectedGarmentIds.isEmpty()

        if (occasionMissing || garmentsMissing) {
            _uiState.update {
                it.copy(occasionError = occasionMissing, garmentsError = garmentsMissing)
            }
            return null
        }

        val chosen = state.availableGarments.filter { it.id in state.selectedGarmentIds }
        return Outfit(
            id = System.currentTimeMillis().toString(),
            garments = chosen,
            paletteHex = chosen.map { it.colorHex }.distinct()
        )
    }
}

class PlanDayViewModelFactory(private val dao: GarmentDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlanDayViewModel(dao) as T
    }
}