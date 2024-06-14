package edu.mirea.onebeattrue.gazpromtestweather.data.network.api

import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.ForecastDto
import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): WeatherDto

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String
    ): WeatherDto

    @GET("/data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): ForecastDto

    @GET("/data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String
    ): ForecastDto

}