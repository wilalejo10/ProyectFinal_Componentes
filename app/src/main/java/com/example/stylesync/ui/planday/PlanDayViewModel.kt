package com.example.stylesync.ui.planday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.Weather
import com.example.stylesync.data.repository.GarmentRepository
import com.example.stylesync.domain.outfit.OutfitGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.stylesync.data.repository.WeatherRepository
import com.example.stylesync.data.local.entity.GarmentEntity

class PlanDayViewModel(private val repository: GarmentRepository) : ViewModel() {

    private val generator = OutfitGenerator()

    private val _uiState = MutableStateFlow(PlanDayUiState())
    val uiState: StateFlow<PlanDayUiState> = _uiState.asStateFlow()

    init {
        loadGarments()
        viewModelScope.launch {
            if (WeatherRepository.latest.value == null) WeatherRepository.refresh()
        }
    }

    private fun loadGarments() {
        viewModelScope.launch {
            repository.garments.collect { garments ->
                _uiState.update { it.copy(availableGarments = garments) }
            }
        }
    }

    fun onOccasionSelected(occasion: Occasion) {
        _uiState.update {
            it.copy(occasion = occasion, occasionError = false, generateError = false)
        }
    }

    fun onGarmentToggled(garmentId: Int) {
        _uiState.update { state ->
            val newSelection =
                if (garmentId in state.selectedGarmentIds) state.selectedGarmentIds - garmentId
                else state.selectedGarmentIds + garmentId
            state.copy(selectedGarmentIds = newSelection, garmentsError = false, generateError = false)
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
        return buildOutfit(chosen, state.occasion)
    }

    fun onGenerateWithColorClick(weather: Weather? = WeatherRepository.latest.value): Outfit? {
        val state = _uiState.value
        val occasion = state.occasion
        if (occasion == null) {
            _uiState.update { it.copy(occasionError = true) }
            return null
        }

        val chosen = generator.generate(
            garments = state.availableGarments,
            occasion = occasion,
            weather = weather,
            usedGarmentIds = garmentIdsUsedThisWeek()
        )
        if (chosen == null) {
            _uiState.update { it.copy(generateError = true) }
            return null
        }

        _uiState.update {
            it.copy(
                selectedGarmentIds = chosen.map { garment -> garment.id }.toSet(),
                garmentsError = false,
                generateError = false
            )
        }
        return buildOutfit(chosen, occasion)
    }

    private fun garmentIdsUsedThisWeek(): Set<Int> {
        val day = PlanStore.selectedDay
        return PlanStore.week.value
            .filterNot { day != null && it.dayLabel == day.dayLabel && it.dayNumber == day.dayNumber }
            .flatMap { it.outfit?.garments.orEmpty() }
            .map { it.id }
            .toSet()
    }
    private fun buildOutfit(garments: List<GarmentEntity>, occasion: Occasion?) = Outfit(
        id = System.currentTimeMillis().toString(),
        garments = garments,
        paletteHex = garments.map { it.colorHex }.distinct(),
        occasion = occasion
    )
}