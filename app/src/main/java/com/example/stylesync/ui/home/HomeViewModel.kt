package com.example.stylesync.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.model.Weather
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {

    // TEMPORAL: el clima real es de la Entrega 2. El outfit sí viene del último que se armó a mano.
    private val weather = Weather("Bogotá", 18, "Nublado")

    val uiState: StateFlow<HomeUiState> = PlanStore.lastOutfit
        .map { HomeUiState(weather = weather, todayOutfit = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState(weather = weather))
}