package com.example.stylesync.ui.outfitresult

import androidx.lifecycle.ViewModel
import com.example.stylesync.data.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OutfitResultViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OutfitResultUiState(outfit = SampleData.outfit))
    val uiState: StateFlow<OutfitResultUiState> = _uiState.asStateFlow()
}