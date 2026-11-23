package com.example.stylesync.domain.outfit

import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Weather
import com.example.stylesync.domain.color.ColorMatch
import com.example.stylesync.domain.color.ColorMatcher
import kotlin.random.Random

class OutfitGenerator(
    private val matcher: ColorMatcher = ColorMatcher(),
    private val random: Random = Random.Default
) {

    fun generate(
        garments: List<GarmentEntity>,
        occasion: Occasion,
        weather: Weather? = null,
        usedGarmentIds: Set<Int> = emptySet()
    ): List<GarmentEntity>? {
        val available = garments.filter { it.id !in usedGarmentIds }
        val tops = available.filter { it.category == CATEGORY_TOP }
        val bottoms = available.filter { it.category == CATEGORY_BOTTOM }
        val shoes = available.filter { it.category == CATEGORY_SHOES }
        val accessories = available.filter { it.category == CATEGORY_ACCESSORY }

        if (tops.isEmpty() || bottoms.isEmpty() || shoes.isEmpty()) return null

        val (top, bottom) = tops
            .flatMap { t -> bottoms.map { b -> t to b } }
            .maxByOrNull { (t, b) ->
                matchScore(t, b) +
                        styleScore(t, occasion, weather) +
                        styleScore(b, occasion, weather) +
                        jitter()
            } ?: return null

        val shoe = shoes.maxByOrNull { s ->
            matchScore(s, top) + matchScore(s, bottom) + styleScore(s, occasion, weather) + jitter()
        } ?: return null

        val accessory = accessories
            .filter { matcher.combine(it.colorHex, top.colorHex) && matcher.combine(it.colorHex, bottom.colorHex) }
            .maxByOrNull { styleScore(it, occasion, weather) + jitter() }

        return listOfNotNull(top, bottom, shoe, accessory)
    }

    private fun matchScore(a: GarmentEntity, b: GarmentEntity): Float =
        when (matcher.matchType(a.colorHex, b.colorHex)) {
            ColorMatch.ANALOGOUS -> 3f
            ColorMatch.NEUTRAL -> 2f
            ColorMatch.COMPLEMENTARY -> 2f
            ColorMatch.NONE -> 0f
        }

    private fun styleScore(garment: GarmentEntity, occasion: Occasion, weather: Weather?): Float =
        occasionScore(garment, occasion) + climateScore(garment, weather)

    private fun occasionScore(garment: GarmentEntity, occasion: Occasion): Float {
        val neutral = matcher.isNeutral(garment.colorHex)
        val vivid = (matcher.saturationOf(garment.colorHex) ?: 0f) > VIVID_MIN_SATURATION
        return when (occasion) {
            Occasion.WORK -> if (neutral) 1f else if (vivid) -1f else 0f
            Occasion.PARTY -> if (!neutral && vivid) 1f else 0f
            Occasion.CASUAL, Occasion.SPORT -> 0f
        }
    }

    private fun climateScore(garment: GarmentEntity, weather: Weather?): Float {
        val temperature = weather?.temperatureC ?: return 0f
        val brightness = matcher.brightnessOf(garment.colorHex) ?: return 0f
        return when {
            temperature >= HOT_FROM_C ->
                if (brightness >= LIGHT_MIN_BRIGHTNESS) 1f else if (brightness < DARK_MAX_BRIGHTNESS) -0.5f else 0f
            temperature <= COLD_UP_TO_C ->
                if (brightness < COLD_PREFERRED_MAX_BRIGHTNESS) 0.5f else 0f
            else -> 0f
        }
    }

    private fun jitter(): Float = random.nextFloat() * 0.1f

    private companion object {
        const val CATEGORY_TOP = "Top"
        const val CATEGORY_BOTTOM = "Bottom"
        const val CATEGORY_SHOES = "Calzado"
        const val CATEGORY_ACCESSORY = "Accesorio"

        const val VIVID_MIN_SATURATION = 0.6f
        const val HOT_FROM_C = 24
        const val COLD_UP_TO_C = 14
        const val LIGHT_MIN_BRIGHTNESS = 0.7f
        const val DARK_MAX_BRIGHTNESS = 0.3f
        const val COLD_PREFERRED_MAX_BRIGHTNESS = 0.5f
    }
}