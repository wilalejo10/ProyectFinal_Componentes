package com.example.stylesync.ui.planday

import androidx.lifecycle.ViewModel
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlanDayViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlanDayUiState(availableGarments = SampleData.garments)
    )
    val uiState: StateFlow<PlanDayUiState> = _uiState.asStateFlow()

    fun onOccasionSelected(occasion: Occasion) {
        _uiState.update { it.copy(occasion = occasion, occasionError = false) }
    }
    fun onGarmentToggled(garmentId: String) {
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