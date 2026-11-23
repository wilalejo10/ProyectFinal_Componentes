package com.example.stylesync.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherConditionTest {

    @Test
    fun code0_isClear() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWmoCode(0))
    }

    @Test
    fun codes1and2_arePartlyCloudy_andCode3_isCloudy() {
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherCondition.fromWmoCode(1))
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherCondition.fromWmoCode(2))
        assertEquals(WeatherCondition.CLOUDY, WeatherCondition.fromWmoCode(3))
    }

    @Test
    fun fogCodes() {
        assertEquals(WeatherCondition.FOG, WeatherCondition.fromWmoCode(45))
        assertEquals(WeatherCondition.FOG, WeatherCondition.fromWmoCode(48))
    }

    @Test
    fun drizzleRange() {
        assertEquals(WeatherCondition.DRIZZLE, WeatherCondition.fromWmoCode(51))
        assertEquals(WeatherCondition.DRIZZLE, WeatherCondition.fromWmoCode(57))
    }

    @Test
    fun rainCodes_includeShowers() {
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(61))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(67))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(81))
    }

    @Test
    fun snowCodes_includeSnowShowers() {
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromWmoCode(73))
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromWmoCode(85))
    }

    @Test
    fun stormCodes() {
        assertEquals(WeatherCondition.STORM, WeatherCondition.fromWmoCode(95))
        assertEquals(WeatherCondition.STORM, WeatherCondition.fromWmoCode(99))
    }

    @Test
    fun unknownOrMissingCode_isUnknown() {
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(999))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(null))
    }
}