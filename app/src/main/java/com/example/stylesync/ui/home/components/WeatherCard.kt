package com.example.stylesync.ui.home.components

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
import com.example.stylesync.data.model.Weather

@Composable
fun WeatherCard(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(weather.city, style = MaterialTheme.typography.titleMedium)

            Text(
                text = stringResource(R.string.weather_temperature, weather.temperatureC),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(weather.condition)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
    WeatherCard(Weather("Ciudad de ejemplo", 18, "Nublado"))
}