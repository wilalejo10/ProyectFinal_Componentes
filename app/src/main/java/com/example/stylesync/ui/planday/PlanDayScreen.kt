package com.example.stylesync.ui.planday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.Occasion
import com.example.stylesync.data.model.SampleData
import com.example.stylesync.ui.planday.components.OccasionSelector

@Composable
fun PlanDayScreen(
    uiState: PlanDayUiState,
    onOccasionSelected: (Occasion) -> Unit,
    onGarmentToggled: (String) -> Unit,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.plan_day_title),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(R.string.plan_day_occasion_label),
            style = MaterialTheme.typography.titleMedium
        )
        OccasionSelector(selected = uiState.occasion, onSelected = onOccasionSelected)
        if (uiState.occasionError) {
            Text(
                text = stringResource(R.string.plan_day_error_occasion),
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = stringResource(R.string.plan_day_garments_label),
            style = MaterialTheme.typography.titleMedium
        )
        uiState.availableGarments.forEach { garment ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = garment.id in uiState.selectedGarmentIds,
                    onCheckedChange = { onGarmentToggled(garment.id) }
                )
                Text("${garment.category}: ${garment.name}")
            }
        }
        if (uiState.garmentsError) {
            Text(
                text = stringResource(R.string.plan_day_error_garments),
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(onClick = onGenerateClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.plan_day_generate))
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun PlanDayScreenPreview() {
    PlanDayScreen(
        uiState = PlanDayUiState(
            availableGarments = SampleData.garments,
            occasionError = true
        ),
        onOccasionSelected = {},
        onGarmentToggled = {},
        onGenerateClick = {}
    )
}