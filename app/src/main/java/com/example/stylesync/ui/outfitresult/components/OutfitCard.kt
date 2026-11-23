package com.example.stylesync.ui.outfitresult.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val visibleState = remember(outfit.id) {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 6 },
        modifier = modifier
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                outfit.garments.forEach { garment ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(hexToColor(garment.colorHex))
                        )
                        Text("${garment.category}: ${garment.name}")
                    }
                }
                Text(
                    text = stringResource(R.string.result_palette_label),
                    style = MaterialTheme.typography.titleSmall
                )
                ColorPaletteRow(outfit.paletteHex)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutfitCardPreview() {
    OutfitCard(SampleData.outfit)
}