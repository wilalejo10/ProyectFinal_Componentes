package com.example.stylesync.ui.weekplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.ui.weekplan.components.WeekRow

@Composable
fun WeekPlanScreen(
    uiState: WeekPlanUiState,
    onDayClick: (Int) -> Unit,
    onPlanDayClick: (DayPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        Text(
            text = stringResource(R.string.week_plan_title),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        WeekRow(uiState.days, uiState.selectedIndex, onDayClick)
        Spacer(Modifier.height(24.dp))
        // El botón solo aparece si hay un día seleccionado válido.
        uiState.days.getOrNull(uiState.selectedIndex)?.let { day ->
            Button(
                onClick = { onPlanDayClick(day) },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.week_plan_plan_day, day.dayLabel, day.dayNumber))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekPlanScreenPreview() {
    WeekPlanScreen(
        uiState = WeekPlanViewModel().uiState.value,
        onDayClick = {},
        onPlanDayClick = {}
    )
}