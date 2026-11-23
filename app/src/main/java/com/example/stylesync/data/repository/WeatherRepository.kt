package com.example.stylesync.data.repository

import com.example.stylesync.data.model.Weather
import com.example.stylesync.data.model.WeatherCondition
import com.example.stylesync.data.remote.WeatherApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

object WeatherRepository {

    private const val BASE_URL = "https://api.open-meteo.com/"

    private const val CITY_NAME = "Bogot\u00E1"
    private const val LATITUDE = 4.711
    private const val LONGITUDE = -74.0721

    private val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    private val _latest = MutableStateFlow<Weather?>(null)

    val latest: StateFlow<Weather?> = _latest.asStateFlow()

    suspend fun refresh(): Weather? {
        return try {
            val current = api.currentWeather(
                latitude = LATITUDE,
                longitude = LONGITUDE,
                current = "temperature_2m,weather_code",
                timezone = "auto"
            ).current
            val temperature = current?.temperature ?: return null

            val weather = Weather(
                city = CITY_NAME,
                temperatureC = temperature.roundToInt(),
                condition = "",
                conditionType = WeatherCondition.fromWmoCode(current.weatherCode)
            )
            _latest.value = weather
            weather
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }
}