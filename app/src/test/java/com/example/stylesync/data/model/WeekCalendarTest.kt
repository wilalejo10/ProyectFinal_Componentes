package com.example.stylesync.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

class WeekCalendarTest {

    @Test
    fun weekHasSevenDays_fromMondayToSunday() {

        val week = WeekCalendar.currentWeek(LocalDate.of(2026, 11, 18), Locale.US)
        assertEquals(7, week.size)
        assertEquals("2026-11-16", week.first().date) // lunes
        assertEquals("2026-11-22", week.last().date)  // domingo
    }

    @Test
    fun whenTodayIsMonday_theWeekStartsToday() {
        val week = WeekCalendar.currentWeek(LocalDate.of(2026, 11, 16), Locale.US)
        assertEquals("2026-11-16", week.first().date)
    }

    @Test
    fun whenTodayIsSunday_theWeekEndsToday() {
        val week = WeekCalendar.currentWeek(LocalDate.of(2026, 11, 22), Locale.US)
        assertEquals("2026-11-16", week.first().date)
        assertEquals("2026-11-22", week.last().date)
    }

    @Test
    fun weekCanCrossTheMonthBoundary() {
        val week = WeekCalendar.currentWeek(LocalDate.of(2026, 12, 2), Locale.US)
        assertEquals(listOf(30, 1, 2, 3, 4, 5, 6), week.map { it.dayNumber })
        assertEquals("2026-11-30", week.first().date)
        assertEquals("2026-12-06", week.last().date)
    }

    @Test
    fun labelsFollowTheLocale() {
        val english = WeekCalendar.currentWeek(LocalDate.of(2026, 11, 18), Locale.US)
        assertEquals("Mon", english.first().dayLabel)
        assertEquals("Sun", english.last().dayLabel)
    }
}
