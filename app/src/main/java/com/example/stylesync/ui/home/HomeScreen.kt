package com.example.stylesync.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stylesync.data.model.SampleData
import com.example.stylesync.data.model.Weather
import com.example.stylesync.data.model.WeatherCondition
import com.example.stylesync.ui.home.components.TodayOutfitSection
import com.example.stylesync.ui.home.components.WeatherSection
import androidx.compose.animation.animateContentSize

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onPlanWeekClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRetryWeather: () -> Unit = {}
) {
    Column(
        modifier = modifier.animateContentSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherSection(
            weather = uiState.weather,
            loading = uiState.weatherLoading,
            error = uiState.weatherError,
            onRetry = onRetryWeather
        )
        TodayOutfitSection(
            outfit = uiState.todayOutfit,
            onPlanClick = onPlanWeekClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUiState(
            weather = Weather("Ciudad de ejemplo", 18, "", WeatherCondition.CLOUDY),
            weatherLoading = false,
            todayOutfit = SampleData.outfit
        ),
        onPlanWeekClick = {}
    )
}