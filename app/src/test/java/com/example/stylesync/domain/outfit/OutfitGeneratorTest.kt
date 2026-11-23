package com.example.stylesync.domain.outfit

import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.Weather
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class OutfitGeneratorTest {

    private val generator = OutfitGenerator(random = Random(42))

    private fun garment(id: Int, category: String, hex: String) =
        GarmentEntity(id = id, name = "prenda$id", category = category, colorHex = hex, imageUri = "")

    private fun ids(result: List<GarmentEntity>?): Set<Int>? = result?.map { it.id }?.toSet()

    @Test
    fun returnsNull_whenThereAreNoShoes() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#000000")
        )
        assertNull(generator.generate(garments, Occasion.CASUAL))
    }

    @Test
    fun returnsNull_whenAllShoesAreAlreadyUsed() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#000000"),
            garment(3, "Calzado", "#000000")
        )
        assertNull(generator.generate(garments, Occasion.CASUAL, usedGarmentIds = setOf(3)))
    }

    @Test
    fun buildsTopBottomAndShoes() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#FF4000"),
            garment(3, "Calzado", "#000000")
        )
        assertEquals(setOf(1, 2, 3), ids(generator.generate(garments, Occasion.CASUAL)))
    }

    @Test
    fun doesNotRepeatGarmentsUsedDuringTheWeek() {
        val garments = listOf(
            garment(1, "Top", "#000000"),
            garment(2, "Top", "#FFFFFF"),
            garment(3, "Bottom", "#808080"),
            garment(4, "Calzado", "#000000")
        )
        val result = ids(generator.generate(garments, Occasion.CASUAL, usedGarmentIds = setOf(1)))
        assertTrue(result!!.contains(2))
        assertFalse(result.contains(1))
    }

    @Test
    fun prefersTheCombiningPair() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#0000FF"),
            garment(3, "Bottom", "#FF4000"),
            garment(4, "Calzado", "#000000")
        )
        val result = ids(generator.generate(garments, Occasion.CASUAL))
        assertTrue(result!!.contains(3))
        assertFalse(result.contains(2))
    }

    @Test
    fun work_prefersNeutralColors() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"), // rojo vivo
            garment(2, "Top", "#808080"), // gris
            garment(3, "Bottom", "#000000"),
            garment(4, "Calzado", "#FFFFFF")
        )
        val result = ids(generator.generate(garments, Occasion.WORK))
        assertTrue(result!!.contains(2))
        assertFalse(result.contains(1))
    }

    @Test
    fun hotWeather_prefersLightColors() {
        val garments = listOf(
            garment(1, "Top", "#001F3F"), // azul oscuro
            garment(2, "Top", "#FFFFFF"), // blanco
            garment(3, "Bottom", "#808080"),
            garment(4, "Calzado", "#000000")
        )
        val hot = Weather("Ciudad de ejemplo", 30, "Soleado")
        val result = ids(generator.generate(garments, Occasion.CASUAL, hot))
        assertTrue(result!!.contains(2))
        assertFalse(result.contains(1))
    }

    @Test
    fun accessoryThatDoesNotCombine_isLeftOut() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#FF4000"),
            garment(3, "Calzado", "#000000"),
            garment(4, "Accesorio", "#0000FF") // azul: no combina con rojo ni naranja
        )
        assertEquals(setOf(1, 2, 3), ids(generator.generate(garments, Occasion.CASUAL)))
    }

    @Test
    fun accessoryThatCombines_isAdded() {
        val garments = listOf(
            garment(1, "Top", "#FF0000"),
            garment(2, "Bottom", "#FF4000"),
            garment(3, "Calzado", "#000000"),
            garment(4, "Accesorio", "#FFFFFF") // blanco: neutro, combina con todo
        )
        assertEquals(setOf(1, 2, 3, 4), ids(generator.generate(garments, Occasion.CASUAL)))
    }
}