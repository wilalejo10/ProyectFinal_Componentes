package com.example.stylesync.data

import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.data.model.Outfit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Almacén en memoria para la Entrega 1: pasa el día elegido y el outfit armado
 * entre Plan semanal -> Planificar día -> Resultado -> Home.
 * En la Entrega 2 se reemplaza por Room/Firestore.
 */
object PlanStore {
    var selectedDay: DayPlan? = null

    private val _lastOutfit = MutableStateFlow<Outfit?>(null)
    val lastOutfit: StateFlow<Outfit?> = _lastOutfit

    fun save(outfit: Outfit) {
        _lastOutfit.value = outfit
    }
}