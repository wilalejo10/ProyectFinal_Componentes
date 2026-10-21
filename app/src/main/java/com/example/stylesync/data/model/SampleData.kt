package com.example.stylesync.data.model

// TEMPORAL: datos falsos para desarrollar sin depender de Room.
object SampleData {
    val garments = listOf(
        Garment("g1", "Camiseta blanca", "Top", "#F5F5F5"),
        Garment("g2", "Jeans azules", "Pantalón", "#2F4F7F"),
        Garment("g3", "Tenis negros", "Calzado", "#1A1A1A"),
        Garment("g4", "Chaqueta beige", "Abrigo", "#D2B48C"),
        Garment("g5", "Gorra roja", "Accesorio", "#B22222")
    )

    // Outfit de ejemplo con las 3 primeras prendas.
    val outfit = Outfit(
        id = "o1",
        garments = garments.take(3),
        paletteHex = garments.take(3).map { it.colorHex }
    )
}