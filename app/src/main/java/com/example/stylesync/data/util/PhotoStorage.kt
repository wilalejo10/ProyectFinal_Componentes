package com.example.stylesync.util

import android.content.Context
import android.net.Uri
import java.io.File

/** Guarda las fotos de las prendas en el almacenamiento privado de la app. */
object PhotoStorage {

    private fun dir(context: Context): File =
        File(context.filesDir, "garments").apply { mkdirs() }

    fun newPhotoFile(context: Context): File =
        File(dir(context), "IMG_${System.currentTimeMillis()}.jpg")

    /** Es lo que se guarda en GarmentEntity.imageUri (Coil lo carga directo). */
    fun toUriString(file: File): String = Uri.fromFile(file).toString()

    /** Copia una imagen elegida en la galería a la carpeta privada. Llamar desde un hilo IO. */
    fun copyFromGallery(context: Context, source: Uri): String? {
        return try {
            val target = newPhotoFile(context)
            val input = context.contentResolver.openInputStream(source) ?: return null
            input.use { ins -> target.outputStream().use { out -> ins.copyTo(out) } }
            toUriString(target)
        } catch (e: Exception) {
            null
        }
    }
}