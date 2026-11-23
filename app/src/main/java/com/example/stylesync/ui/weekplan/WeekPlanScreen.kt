package com.example.stylesync.ui.weekplan

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.ui.outfitresult.components.OutfitCard
import com.example.stylesync.ui.weekplan.components.WeekRow

@Composable
fun WeekPlanScreen(
    uiState: WeekPlanUiState,
    onDayClick: (Int) -> Unit,
    onPlanDayClick: (DayPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.week_plan_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        WeekRow(uiState.days, uiState.selectedIndex, onDayClick)
        Spacer(Modifier.height(24.dp))

        uiState.days.getOrNull(uiState.selectedIndex)?.let { day ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val outfit = day.outfit
                if (outfit != null) {
                    OutfitCard(outfit)
                } else {
                    Text(
                        text = stringResource(R.string.week_plan_no_outfit),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(onClick = { onPlanDayClick(day) }) {
                    Text(stringResource(R.string.week_plan_plan_day, day.dayLabel, day.dayNumber))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekPlanScreenPreview() {
    WeekPlanScreen(
        uiState = WeekPlanUiState(
            days = listOf(DayPlan("Lun", 23), DayPlan("Mar", 24), DayPlan("Mié", 25)),
            selectedIndex = 0
        ),
        onDayClick = {},
        onPlanDayClick = {}
    )
}