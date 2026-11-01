package com.example.stylesync.ui.weekplan

import com.example.stylesync.data.model.DayPlan

data class WeekPlanUiState(
    val days: List<DayPlan> = emptyList(),
    val selectedIndex: Int = 0
)