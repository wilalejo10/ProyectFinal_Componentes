package com.example.stylesync.data

import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.WeekCalendar
import com.example.stylesync.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object PlanStore {
    var selectedDay: DayPlan? = null

    private val _week = MutableStateFlow(WeekCalendar.currentWeek())
    val week: StateFlow<List<DayPlan>> = _week

    private val _lastOutfit = MutableStateFlow<Outfit?>(null)
    val lastOutfit: StateFlow<Outfit?> = _lastOutfit

    fun save(outfit: Outfit) {
        _lastOutfit.value = outfit
        val day = selectedDay ?: return

        _week.update { days ->
            days.map {
                if (sameDay(it, day)) it.copy(occasion = outfit.occasion ?: it.occasion, outfit = outfit)
                else it
            }
        }

        val date = _week.value.firstOrNull { sameDay(it, day) }?.date.orEmpty()
        if (date.isNotBlank()) PlanRepository.persist(date, outfit)
    }

    fun replaceWeek(loaded: List<DayPlan>) {
        _week.update { current ->
            loaded.map { fromServer ->
                val inMemory = current.firstOrNull { sameDay(it, fromServer) }
                if (inMemory?.outfit != null) inMemory else fromServer
            }
        }
    }

    fun weekForReport(): List<Pair<String, List<GarmentEntity>>> =
        _week.value.map { "${it.dayLabel} ${it.dayNumber}" to (it.outfit?.garments ?: emptyList()) }

    private fun sameDay(a: DayPlan, b: DayPlan): Boolean =
        if (a.date.isNotBlank() && b.date.isNotBlank()) a.date == b.date
        else a.dayLabel == b.dayLabel && a.dayNumber == b.dayNumber
}