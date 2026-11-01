package com.example.stylesync.ui.planday

import com.example.stylesync.data.model.Garment
import com.example.stylesync.data.model.Occasion

data class PlanDayUiState(
    val availableGarments: List<Garment> = emptyList(),
    val occasion: Occasion? = null,
    val selectedGarmentIds: Set<String> = emptySet(),
    val occasionError: Boolean = false,
    val garmentsError: Boolean = false
)