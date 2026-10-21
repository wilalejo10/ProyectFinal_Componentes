package com.example.stylesync.data.model

data class Outfit(
    val id: String,
    val garments: List<Garment>,
    val paletteHex: List<String>
)