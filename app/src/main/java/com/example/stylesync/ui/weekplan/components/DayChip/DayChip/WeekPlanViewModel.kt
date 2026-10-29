package com.example.stylesync.ui.weekplan

import androidx.lifecycle.ViewModel
import com.example.stylesync.data.model.DayPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WeekPlanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeekPlanUiState(days = sampleWeek()))
    val uiState: StateFlow<WeekPlanUiState> = _uiState.asStateFlow()

    fun onDaySelected(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    private fun sampleWeek() = listOf(
        DayPlan("Lun", 28), DayPlan("Mar", 29), DayPlan("Mié", 30),
        DayPlan("Jue", 1), DayPlan("Vie", 2), DayPlan("Sáb", 3), DayPlan("Dom", 4)
    )
}