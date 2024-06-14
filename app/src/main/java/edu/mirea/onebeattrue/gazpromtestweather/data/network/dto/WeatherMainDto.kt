package edu.mirea.onebeattrue.gazpromtestweather.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherMainDto(
    @SerializedName("temp") val temp: Float
)
