package com.example.stylesync.ui.weekplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.data.PlanStore
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeekPlanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        WeekPlanUiState(
            days = PlanStore.week.value,
            selectedIndex = todayIndex(PlanStore.week.value)
        )
    )
    val uiState: StateFlow<WeekPlanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            PlanStore.week.collect { days -> _uiState.update { it.copy(days = days) } }
        }
        viewModelScope.launch { PlanRepository.loadWeek() }
    }

    fun onDaySelected(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    private fun todayIndex(days: List<DayPlan>): Int =
        days.indexOfFirst { it.date == LocalDate.now().toString() }.coerceAtLeast(0)
}