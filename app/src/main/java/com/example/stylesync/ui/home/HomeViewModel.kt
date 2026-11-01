package com.example.stylesync.ui.home

import androidx.lifecycle.ViewModel
import com.example.stylesync.data.model.SampleData
import com.example.stylesync.data.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // TEMPORAL: clima y outfit falsos. El clima real es de la Entrega 2.
    private val _uiState = MutableStateFlow(
        HomeUiState(
            weather = Weather("Ciudad de ejemplo", 18, "Nublado"),
            todayOutfit = SampleData.outfit
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}