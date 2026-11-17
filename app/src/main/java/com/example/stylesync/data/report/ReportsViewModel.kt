package com.example.stylesync.ui.reports

import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.R
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.data.report.ReportFiles
import com.example.stylesync.data.report.ReportGenerator
import com.example.stylesync.data.repository.GarmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface ReportState {
    data object Idle : ReportState
    data object Working : ReportState
    data class Success(val fileName: String, val uri: Uri, val mime: String) : ReportState
    data class Error(@StringRes val message: Int) : ReportState
}

class ReportsViewModel(
    private val appContext: Context,
    private val repo: GarmentRepository
) : ViewModel() {

    var state by mutableStateOf<ReportState>(ReportState.Idle)
        private set

    fun exportInventoryCsv() =
        export("inventario_${today()}.csv", "text/csv") {
            ReportGenerator.inventoryCsv(repo.getAll()).toByteArray(Charsets.UTF_8)
        }

    /** week = lista de (nombre del dia, prendas de ese dia), 7 elementos. */
    fun exportWeeklyPdf(week: List<Pair<String, List<GarmentEntity>>>) =
        export("outfit_semanal_${today()}.pdf", "application/pdf") {
            ByteArrayOutputStream().also {
                ReportGenerator.weeklyPdf(it, "Outfit semanal", week)
            }.toByteArray()
        }

    fun dismiss() {
        state = ReportState.Idle
    }

    private fun export(fileName: String, mime: String, produce: suspend () -> ByteArray) {
        if (state is ReportState.Working) return
        state = ReportState.Working
        viewModelScope.launch {
            state = try {
                val uri = withContext(Dispatchers.IO) {
                    ReportFiles.saveToDownloads(appContext, fileName, mime, produce())
                }
                ReportState.Success(fileName, uri, mime)
            } catch (e: Exception) {
                ReportState.Error(R.string.report_error)
            }
        }
    }

    private fun today() = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
}