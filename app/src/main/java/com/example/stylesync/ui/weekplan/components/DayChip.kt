package com.example.stylesync.ui.weekplan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.data.model.DayPlan
import com.example.stylesync.data.model.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayChip(
    day: DayPlan,
    selected: Boolean,
    onClick: () -> Unit,
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
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (day.outfit != null) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DayChipPreview() {
    DayChip(DayPlan("Lun", 28, outfit = SampleData.outfit), selected = true, onClick = {})
}