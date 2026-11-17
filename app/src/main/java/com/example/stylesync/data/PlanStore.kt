package com.example.stylesync.data

import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.data.model.Outfit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Almacen en memoria del plan semanal: una sola fuente de verdad para
 * Plan semanal -> Planificar dia -> Resultado -> Home -> Reportes.
 */
object PlanStore {
    var selectedDay: DayPlan? = null

    private val _week = MutableStateFlow(
        listOf(
            DayPlan("Lun", 28), DayPlan("Mar", 29), DayPlan("Mi\u00E9", 30),
            DayPlan("Jue", 1), DayPlan("Vie", 2), DayPlan("S\u00E1b", 3), DayPlan("Dom", 4)
        )
    )
    val week: StateFlow<List<DayPlan>> = _week

    private val _lastOutfit = MutableStateFlow<Outfit?>(null)
    val lastOutfit: StateFlow<Outfit?> = _lastOutfit

    fun save(outfit: Outfit) {
        _lastOutfit.value = outfit
        val day = selectedDay ?: return
        _week.update { days ->
            days.map {
                if (it.dayLabel == day.dayLabel && it.dayNumber == day.dayNumber) it.copy(outfit = outfit)
                else it
            }
        }
    }

    /** Los 7 dias con sus prendas, para el PDF. */
    fun weekForReport(): List<Pair<String, List<GarmentEntity>>> =
        _week.value.map { "${it.dayLabel} ${it.dayNumber}" to (it.outfit?.garments ?: emptyList()) }
}