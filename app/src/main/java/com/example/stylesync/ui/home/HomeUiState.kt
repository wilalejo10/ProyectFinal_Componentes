package com.example.stylesync.ui.home

import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.Weather

data class HomeUiState(
    val weather: Weather? = null,
    val weatherLoading: Boolean = true,
    val weatherError: Boolean = false,
    val todayOutfit: Outfit? = null
)