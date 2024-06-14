package edu.mirea.onebeattrue.gazpromtestweather.data.mapper

import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.WeatherApiFactory
import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.ForecastDto
import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.WeatherDto
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather

fun WeatherDto.toEntity(): Weather = Weather(
    date = date,
    title = details.first().title,
    description = details.first().description,
    iconUrl = WeatherApiFactory.BASE_URL + "/img/wn/${details.first().icon}@2x.png",
    temp = main.temp
)

fun ForecastDto.toEntity(): Forecast = Forecast(
    upcoming = weatherList.map { it.toEntity() }
)
