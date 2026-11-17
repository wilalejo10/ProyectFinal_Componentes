package com.example.stylesync.ui.weekplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeekPlanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeekPlanUiState(days = PlanStore.week.value))
    val uiState: StateFlow<WeekPlanUiState> = _uiState.asStateFlow()

    init {
        // cuando se guarda un outfit, el plan semanal se actualiza solo
        viewModelScope.launch {
            PlanStore.week.collect { days -> _uiState.update { it.copy(days = days) } }
        }
    }

    fun onDaySelected(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }
}