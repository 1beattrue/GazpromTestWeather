package edu.mirea.onebeattrue.gazpromtestweather.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val date: Long,
    val title: String,
    val description: String,
    val iconUrl: String,
    val temp: Float
)
