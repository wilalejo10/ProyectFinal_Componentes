package com.example.stylesync.domain.color

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorMatcherTest {

    private val matcher = ColorMatcher()

    @Test
    fun blackWhiteGrayAndBeige_areNeutral() {
        assertTrue(matcher.isNeutral("#000000")) // negro
        assertTrue(matcher.isNeutral("#1A1A1A")) // casi negro
        assertTrue(matcher.isNeutral("#FFFFFF")) // blanco
        assertTrue(matcher.isNeutral("#808080")) // gris
        assertTrue(matcher.isNeutral("#D2B48C")) // beige
    }

    @Test
    fun strongColors_areNotNeutral() {
        assertFalse(matcher.isNeutral("#FF0000")) // rojo
        assertFalse(matcher.isNeutral("#0000FF")) // azul
    }

    @Test
    fun neutralCombinesWithAnyColor() {
        assertEquals(ColorMatch.NEUTRAL, matcher.matchType("#000000", "#FF0000"))
        assertEquals(ColorMatch.NEUTRAL, matcher.matchType("#0000FF", "#D2B48C"))
    }

    @Test
    fun neighborHues_areAnalogous() {
        assertEquals(ColorMatch.ANALOGOUS, matcher.matchType("#FF0000", "#FF4000"))
    }

    @Test
    fun analogousWorksAcrossTheCircleEdge() {
        assertEquals(ColorMatch.ANALOGOUS, matcher.matchType("#FF0055", "#FF1A00"))
    }

    @Test
    fun farHues_thatAreNotOpposite_doNotMatch() {
        assertEquals(ColorMatch.NONE, matcher.matchType("#FF0000", "#0000FF")) // 120 grados
        assertEquals(ColorMatch.NONE, matcher.matchType("#FF0000", "#00FF00")) // 120 grados
    }

    @Test
    fun oppositeStrongColors_doNotMatch() {
        assertEquals(ColorMatch.NONE, matcher.matchType("#0000FF", "#FFFF00"))
    }

    @Test
    fun oppositeColors_matchWhenOneIsSoft() {
        assertEquals(ColorMatch.COMPLEMENTARY, matcher.matchType("#0000FF", "#FFFF99"))
    }

    @Test
    fun matchIsSymmetric() {
        val pairs = listOf(
            "#FF0000" to "#FF4000",
            "#0000FF" to "#FFFF99",
            "#FF0000" to "#00FF00",
            "#000000" to "#FF0000"
        )
        for ((a, b) in pairs) {
            assertEquals(matcher.matchType(a, b), matcher.matchType(b, a))
        }
    }

    @Test
    fun combine_isTrueOnlyWhenThereIsAMatch() {
        assertTrue(matcher.combine("#000000", "#FF0000"))
        assertFalse(matcher.combine("#FF0000", "#0000FF"))
    }

    @Test
    fun hexWithoutHash_isAccepted() {
        assertTrue(matcher.combine("FF0000", "FF4000"))
    }

    @Test
    fun invalidHex_doesNotMatch() {
        assertEquals(ColorMatch.NONE, matcher.matchType("xyz", "#FF0000"))
        assertEquals(ColorMatch.NONE, matcher.matchType("#12345", "#FF0000"))
        assertFalse(matcher.combine("#GGGGGG", "#FF0000"))
    }
}