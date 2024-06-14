package edu.mirea.onebeattrue.gazpromtestweather.data.network.api

import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.CityDto
import retrofit2.http.GET

interface CityApiService {
    @GET
    suspend fun getCities(): List<CityDto>
}