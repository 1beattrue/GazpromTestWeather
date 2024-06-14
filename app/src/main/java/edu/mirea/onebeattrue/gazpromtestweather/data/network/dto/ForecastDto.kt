package edu.mirea.onebeattrue.gazpromtestweather.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("list") val weatherList: List<WeatherDto>
)