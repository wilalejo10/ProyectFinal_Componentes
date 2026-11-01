package com.example.stylesync.ui.planday

import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.model.Occasion

data class PlanDayUiState(
    val availableGarments: List<GarmentEntity> = emptyList(),
    val occasion: Occasion? = null,
    val selectedGarmentIds: Set<Int> = emptySet(),
    val occasionError: Boolean = false,
    val garmentsError: Boolean = false
)