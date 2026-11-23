package com.example.stylesync.domain.color

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PaletteColorExtractor {

    private const val MAX_SIDE_PX = 200

    suspend fun dominantHex(imageUri: String): String? = withContext(Dispatchers.Default) {
        val path = imageUri.toUri().path ?: return@withContext null
        val bitmap = decodeSmall(path) ?: return@withContext null

        val palette = Palette.from(bitmap).maximumColorCount(16).generate()
        val swatch = palette.dominantSwatch ?: return@withContext null

        String.format("#%06X", swatch.rgb and 0xFFFFFF)
    }

    private fun decodeSmall(path: String): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (bounds.outWidth / sample > MAX_SIDE_PX * 2 ||
            bounds.outHeight / sample > MAX_SIDE_PX * 2
        ) {
            sample *= 2
        }
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return BitmapFactory.decodeFile(path, options)
    }
}