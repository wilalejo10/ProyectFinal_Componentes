package com.example.stylesync.ui.home

import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.Weather

data class HomeUiState(
    val weather: Weather,
    val todayOutfit: Outfit? = null
)