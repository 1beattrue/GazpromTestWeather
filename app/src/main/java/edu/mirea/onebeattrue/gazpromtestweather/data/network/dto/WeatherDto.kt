package edu.mirea.onebeattrue.gazpromtestweather.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("weather") val details: List<WeatherDetailsDto>,
    @SerializedName("dt") val date: Long,
    @SerializedName("main") val main: WeatherMainDto,
)