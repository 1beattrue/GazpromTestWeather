package edu.mirea.onebeattrue.gazpromtestweather.data.mapper

import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.ForecastDto
import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.WeatherDto
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherDto.toEntity(): Weather = Weather(
    date = date.toCalendar(),
    title = details.first().title,
    description = details.first().description,
    iconUrl = "https://openweathermap.org/img/wn/${details.first().icon}@4x.png",
    temp = main.temp
)

fun ForecastDto.toEntity(): Forecast = Forecast(
    upcoming = weatherList.map { it.toEntity() }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}