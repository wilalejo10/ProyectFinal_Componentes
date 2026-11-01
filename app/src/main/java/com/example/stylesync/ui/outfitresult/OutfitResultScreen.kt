package com.example.stylesync.ui.outfitresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.SampleData
import com.example.stylesync.ui.outfitresult.components.OutfitCard

@Composable
fun OutfitResultScreen(
    uiState: OutfitResultUiState,
    onPlanAnotherClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.result_title),
            style = MaterialTheme.typography.headlineSmall
        )
        uiState.outfit?.let { OutfitCard(it) }
        Button(onClick = onPlanAnotherClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.result_plan_another))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutfitResultScreenPreview() {
    OutfitResultScreen(
        uiState = OutfitResultUiState(outfit = SampleData.outfit),
        onPlanAnotherClick = {}
    )
}