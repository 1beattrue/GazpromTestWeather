package edu.mirea.onebeattrue.gazpromtestweather.data.network.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id") val id: String,
    @SerializedName("city") val name: String,
    @SerializedName("latitude") val lat: String,
    @SerializedName("longitude") val lon: String
)