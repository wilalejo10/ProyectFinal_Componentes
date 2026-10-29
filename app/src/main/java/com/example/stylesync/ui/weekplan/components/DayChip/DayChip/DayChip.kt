package com.example.stylesync.ui.weekplan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stylesync.data.model.DayPlan

@OptIn(ExperimentalMaterial3Api::class) // FilterChip es una API marcada como experimental
@Composable
fun DayChip(
    day: DayPlan,
    selected: Boolean,
    onClick: () -> Unit,   // quien usa el chip decide qué pasa al tocarlo
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        label = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(day.dayLabel)
                Text(day.dayNumber.toString())
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DayChipPreview() {
    DayChip(DayPlan("Lun", 28), selected = true, onClick = {})
}