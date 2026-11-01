package com.example.stylesync.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.Outfit
import com.example.stylesync.data.model.SampleData
import com.example.stylesync.ui.outfitresult.components.OutfitCard

@Composable
fun TodayOutfitSection(
    outfit: Outfit?,
    onPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.home_today_title),
            style = MaterialTheme.typography.titleMedium
        )

        if (outfit == null) {
            Text(stringResource(R.string.home_no_outfit))
            Button(onClick = onPlanClick) {
                Text(stringResource(R.string.home_plan_week))
            }
        } else {
            OutfitCard(outfit)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayOutfitSectionPreview() {
    TodayOutfitSection(outfit = SampleData.outfit, onPlanClick = {})
}