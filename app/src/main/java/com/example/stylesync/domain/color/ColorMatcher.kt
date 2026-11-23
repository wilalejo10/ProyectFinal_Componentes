package com.example.stylesync.domain.color

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class ColorMatch { NEUTRAL, ANALOGOUS, COMPLEMENTARY, NONE }

class ColorMatcher {

    private data class Hsv(val hue: Float, val saturation: Float, val value: Float)

    fun matchType(hexA: String, hexB: String): ColorMatch {

        val a = parseHsv(hexA) ?: return ColorMatch.NONE
        val b = parseHsv(hexB) ?: return ColorMatch.NONE

        if (isNeutralColor(a) || isNeutralColor(b)) return ColorMatch.NEUTRAL

        val distance = hueDistance(a.hue, b.hue)

        if (distance <= ANALOGOUS_MAX_DISTANCE) return ColorMatch.ANALOGOUS

        if (distance >= COMPLEMENTARY_MIN_DISTANCE && (isSoft(a) || isSoft(b))) {
            return ColorMatch.COMPLEMENTARY
        }

        return ColorMatch.NONE
    }

    fun combine(hexA: String, hexB: String): Boolean =
        matchType(hexA, hexB) != ColorMatch.NONE

    fun isNeutral(hex: String): Boolean {
        val color = parseHsv(hex) ?: return false
        return isNeutralColor(color)
    }

    fun saturationOf(hex: String): Float? = parseHsv(hex)?.saturation

    fun brightnessOf(hex: String): Float? = parseHsv(hex)?.value

    private fun isNeutralColor(c: Hsv): Boolean {
        val isBlack = c.value < BLACK_MAX_VALUE
        val isWhiteOrGray = c.saturation < GRAY_MAX_SATURATION
        val isBeige = c.hue in BEIGE_MIN_HUE..BEIGE_MAX_HUE &&
                c.saturation < BEIGE_MAX_SATURATION &&
                c.value > BEIGE_MIN_VALUE
        return isBlack || isWhiteOrGray || isBeige
    }

    private fun isSoft(c: Hsv): Boolean = c.saturation <= SOFT_MAX_SATURATION

    private fun hueDistance(h1: Float, h2: Float): Float {
        val diff = abs(h1 - h2)
        return min(diff, 360f - diff)
    }

    private fun parseHsv(hex: String): Hsv? {
        val clean = hex.trim().removePrefix("#")
        if (clean.length != 6) return null
        val rgb = clean.toIntOrNull(16) ?: return null

        val r = ((rgb shr 16) and 0xFF) / 255f
        val g = ((rgb shr 8) and 0xFF) / 255f
        val b = (rgb and 0xFF) / 255f

        val maxC = max(r, max(g, b))
        val minC = min(r, min(g, b))
        val delta = maxC - minC

        var hue = when {
            delta == 0f -> 0f
            maxC == r -> 60f * (((g - b) / delta) % 6f)
            maxC == g -> 60f * (((b - r) / delta) + 2f)
            else -> 60f * (((r - g) / delta) + 4f)
        }
        if (hue < 0f) hue += 360f

        val saturation = if (maxC == 0f) 0f else delta / maxC
        return Hsv(hue, saturation, maxC)
    }

    private companion object {
        const val BLACK_MAX_VALUE = 0.15f
        const val GRAY_MAX_SATURATION = 0.15f
        const val BEIGE_MIN_HUE = 25f
        const val BEIGE_MAX_HUE = 55f
        const val BEIGE_MAX_SATURATION = 0.35f
        const val BEIGE_MIN_VALUE = 0.6f
        const val ANALOGOUS_MAX_DISTANCE = 30f
        const val COMPLEMENTARY_MIN_DISTANCE = 150f
        const val SOFT_MAX_SATURATION = 0.5f
    }
}