package com.example.stylesync.data.report

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.stylesync.data.local.entity.GarmentEntity
import java.io.OutputStream

object ReportGenerator {

    /** CSV del inventario. El BOM al inicio hace que Excel lea bien las tildes. */
    fun inventoryCsv(items: List<GarmentEntity>): String = buildString {
        append('\uFEFF')
        append("Nombre,Categor\u00EDa,Color\r\n")
        items.sortedWith(compareBy({ it.category }, { it.name })).forEach {
            append(csv(it.name)).append(',')
                .append(csv(it.category)).append(',')
                .append(csv(it.colorHex)).append("\r\n")
        }
    }

    private fun csv(v: String) = "\"" + v.replace("\"", "\"\"") + "\""

    /** PDF con los 7 dias y las prendas de cada uno. */
    fun weeklyPdf(out: OutputStream, title: String, week: List<Pair<String, List<GarmentEntity>>>) {
        val pageW = 595
        val pageH = 842
        val margin = 40f
        val titlePaint = Paint().apply { textSize = 20f; isFakeBoldText = true }
        val dayPaint = Paint().apply { textSize = 14f; isFakeBoldText = true }
        val textPaint = Paint().apply { textSize = 12f }
        val grayPaint = Paint().apply { textSize = 12f; color = Color.GRAY }
        val swatch = Paint()

        val pdf = PdfDocument()
        var pageNum = 1
        var page = pdf.startPage(PdfDocument.PageInfo.Builder(pageW, pageH, pageNum).create())
        var canvas = page.canvas
        var y = margin + 20f
        canvas.drawText(title, margin, y, titlePaint)
        y += 36f

        for ((day, items) in week) {
            val needed = 26f + maxOf(1, items.size) * 20f + 12f
            if (y + needed > pageH - margin) {
                pdf.finishPage(page)
                pageNum++
                page = pdf.startPage(PdfDocument.PageInfo.Builder(pageW, pageH, pageNum).create())
                canvas = page.canvas
                y = margin + 20f
            }
            canvas.drawText(day, margin, y, dayPaint)
            y += 22f
            if (items.isEmpty()) {
                canvas.drawText("Sin prendas asignadas", margin + 16f, y, grayPaint)
                y += 20f
            } else {
                items.forEach { g ->
                    swatch.color = runCatching { Color.parseColor(g.colorHex) }.getOrDefault(Color.LTGRAY)
                    canvas.drawRect(margin + 16f, y - 10f, margin + 26f, y, swatch)
                    canvas.drawText("${g.name}  (${g.category})", margin + 34f, y, textPaint)
                    y += 20f
                }
            }
            y += 12f
        }
        pdf.finishPage(page)
        pdf.writeTo(out)
        pdf.close()
    }
}