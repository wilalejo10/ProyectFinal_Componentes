package com.example.stylesync.ui.weekplan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stylesync.data.model.DayPlan

@Composable
fun WeekRow(
    days: List<DayPlan>,
    selectedIndex: Int,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(days) { index, day ->
            DayChip(
                day = day,
                selected = index == selectedIndex,
                onClick = { onDayClick(index) }
            )
        }
    }
}