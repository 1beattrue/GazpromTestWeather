package edu.mirea.onebeattrue.gazpromtestweather.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDetailsDto(
    @SerializedName("main") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)
