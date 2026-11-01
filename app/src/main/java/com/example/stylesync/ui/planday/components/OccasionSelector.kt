package com.example.stylesync.ui.planday.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.Occasion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OccasionSelector(
    selected: Occasion?,
    onSelected: (Occasion) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Occasion.entries) { occasion ->
            FilterChip(
                selected = occasion == selected,
                onClick = { onSelected(occasion) },
                label = { Text(occasionLabel(occasion)) }
            )
        }
    }
}

@Composable
private fun occasionLabel(occasion: Occasion): String = stringResource(
    when (occasion) {
        Occasion.CASUAL -> R.string.occasion_casual
        Occasion.WORK -> R.string.occasion_work
        Occasion.PARTY -> R.string.occasion_party
        Occasion.SPORT -> R.string.occasion_sport
    }
)

@Preview(showBackground = true)
@Composable
private fun OccasionSelectorPreview() {
    OccasionSelector(selected = Occasion.CASUAL, onSelected = {})
}