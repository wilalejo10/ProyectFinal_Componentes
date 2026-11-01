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
import com.example.stylesync.ui.home.components.TodayOutfitSection
import com.example.stylesync.ui.home.components.WeatherCard

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onPlanWeekClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherCard(weather = uiState.weather)
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
            weather = Weather("Bogotá D.C", 18, "Nublado"),
            todayOutfit = SampleData.outfit
        ),
        onPlanWeekClick = {}
    )
}