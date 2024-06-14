package edu.mirea.onebeattrue.gazpromtestweather.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String
)