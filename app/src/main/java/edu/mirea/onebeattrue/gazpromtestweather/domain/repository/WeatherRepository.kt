package edu.mirea.onebeattrue.gazpromtestweather.domain.repository

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather

interface WeatherRepository {
    suspend fun getWeather(lat: Float, lon: Float): Weather
    suspend fun getForecast(lat: Float, lon: Float): Forecast
}