package com.example.stylesync.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.local.entity.GarmentEntity
import com.example.stylesync.ui.components.PrimaryButton
import com.example.stylesync.data.PlanStore

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    modifier: Modifier = Modifier,
    weekProvider: () -> List<Pair<String, List<GarmentEntity>>> = { PlanStore.weekForReport() }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.reports_title), style = MaterialTheme.typography.titleLarge)

        PrimaryButton(
            text = stringResource(R.string.reports_weekly_pdf),
            onClick = { viewModel.exportWeeklyPdf(weekProvider()) },
            loading = viewModel.state is ReportState.Working
        )
        OutlinedButton(
            onClick = { viewModel.exportInventoryCsv() },
            enabled = viewModel.state !is ReportState.Working,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.reports_inventory_csv))
        }

        ReportStatusCard(viewModel.state)
    }
}

/** Semana vacia mientras el plan semanal no se guarda en Room. */
fun emptyWeek(): List<Pair<String, List<GarmentEntity>>> =
    listOf("Lunes", "Martes", "Mi\u00E9rcoles", "Jueves", "Viernes", "S\u00E1bado", "Domingo")
        .map { it to emptyList() }