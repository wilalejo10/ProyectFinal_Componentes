package com.example.stylesync.ui.outfitresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OutfitResultViewModel : ViewModel() {
    // Muestra el outfit que se acaba de armar en "Planificar día" (ya no el de SampleData).
    val uiState: StateFlow<OutfitResultUiState> = PlanStore.lastOutfit
        .map { OutfitResultUiState(outfit = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OutfitResultUiState())
}