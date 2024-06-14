package edu.mirea.onebeattrue.gazpromtestweather.data.repository

import edu.mirea.onebeattrue.gazpromtestweather.data.mapper.toEntity
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.WeatherApiService
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherRepository {
    override suspend fun getWeather(lat: Float, lon: Float): Weather {
        return weatherApiService.getWeather(lat, lon).toEntity()
    }

    override suspend fun getForecast(lat: Float, lon: Float): Forecast {
        return weatherApiService.getForecast(lat, lon).toEntity()
    }
}