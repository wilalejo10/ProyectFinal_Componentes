package com.example.stylesync.data.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object WeekCalendar {

    fun currentWeek(
        today: LocalDate = LocalDate.now(),
        locale: Locale = Locale.getDefault()
    ): List<DayPlan> {
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0..6).map { offset ->
            val date = monday.plusDays(offset.toLong())
            DayPlan(
                dayLabel = date.dayOfWeek
                    .getDisplayName(TextStyle.SHORT, locale)
                    .replaceFirstChar { it.titlecase(locale) },
                dayNumber = date.dayOfMonth,
                date = date.toString()
            )
        }
    }
}