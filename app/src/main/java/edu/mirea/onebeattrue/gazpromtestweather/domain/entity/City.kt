package edu.mirea.onebeattrue.gazpromtestweather.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Long,
    val name: String,
    val lat: Float,
    val lon: Float
)