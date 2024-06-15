package edu.mirea.onebeattrue.gazpromtestweather.domain.entity

import java.util.Calendar

data class Weather(
    val date: Calendar,
    val title: String,
    val description: String,
    val iconUrl: String,
    val temp: Float
)
