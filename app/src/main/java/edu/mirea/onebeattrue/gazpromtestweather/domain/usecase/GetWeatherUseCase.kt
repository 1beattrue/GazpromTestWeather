package edu.mirea.onebeattrue.gazpromtestweather.domain.usecase

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Float, lon: Float): Weather {
        return repository.getWeather(lat, lon)
    }
}