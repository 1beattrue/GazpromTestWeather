package edu.mirea.onebeattrue.gazpromtestweather.domain.usecase

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Float, lon: Float): Forecast {
        return repository.getForecast(lat, lon)
    }
}