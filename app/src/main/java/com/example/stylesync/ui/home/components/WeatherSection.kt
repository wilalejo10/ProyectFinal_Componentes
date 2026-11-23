package com.example.stylesync.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.data.model.Weather
import com.example.stylesync.data.model.WeatherCondition

@Composable
fun WeatherSection(
    weather: Weather?,
    loading: Boolean,
    error: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        weather != null -> WeatherCard(weather, modifier)

        loading -> Card(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Text(stringResource(R.string.weather_loading))
            }
        }

        error -> Card(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.weather_error),
                    color = MaterialTheme.colorScheme.error
                )
                OutlinedButton(onClick = onRetry) {
                    Text(stringResource(R.string.weather_retry))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherSectionLoadingPreview() {
    WeatherSection(weather = null, loading = true, error = false, onRetry = {})
}

@Preview(showBackground = true)
@Composable
private fun WeatherSectionErrorPreview() {
    WeatherSection(weather = null, loading = false, error = true, onRetry = {})
}

@Preview(showBackground = true)
@Composable
private fun WeatherSectionReadyPreview() {
    WeatherSection(
        weather = Weather("Ciudad de ejemplo", 18, "", WeatherCondition.RAIN),
        loading = false,
        error = false,
        onRetry = {}
    )
}