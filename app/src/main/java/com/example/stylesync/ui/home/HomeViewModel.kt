package com.example.stylesync.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.repository.PlanRepository
import com.example.stylesync.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            PlanStore.week.collect { week ->
                val today = LocalDate.now().toString()
                val todayOutfit = week.firstOrNull { it.date == today }?.outfit
                _uiState.update { it.copy(todayOutfit = todayOutfit) }
            }
        }
        viewModelScope.launch { PlanRepository.loadWeek() }
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(weatherLoading = true, weatherError = false) }
            val fresh = WeatherRepository.refresh()
            _uiState.update {
                it.copy(
                    weather = fresh ?: it.weather,
                    weatherLoading = false,
                    weatherError = fresh == null && it.weather == null
                )
            }
        }
    }
}