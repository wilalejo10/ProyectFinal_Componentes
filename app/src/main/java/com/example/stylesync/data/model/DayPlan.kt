package com.example.stylesync.data.model

data class DayPlan(
    val dayLabel: String,
    val dayNumber: Int,
    val occasion: Occasion? = null,
    val outfit: Outfit? = null,
    val date: String = ""
)