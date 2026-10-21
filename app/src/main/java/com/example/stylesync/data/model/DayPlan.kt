package com.example.stylesync.data.model

data class DayPlan(
    val dayLabel: String,           // "Lun"
    val dayNumber: Int,             // 28
    val occasion: Occasion? = null, // vacío si el día aún no se planifica
    val outfit: Outfit? = null
)