package com.example.stylesync.ui.outfitresult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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

@Composable
fun OutfitCard(
    outfit: Outfit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            outfit.garments.forEach { garment ->
                Text("${garment.category}: ${garment.name}")
            }
            Text(
                text = stringResource(R.string.result_palette_label),
                style = MaterialTheme.typography.titleSmall
            )
            ColorPaletteRow(outfit.paletteHex)
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun OutfitCardPreview() {
    OutfitCard(SampleData.outfit)
}