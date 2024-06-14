package edu.mirea.onebeattrue.gazpromtestweather.data.network.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object CityApiFactory {
    private const val BASE_URL =
        "https://gist.githubusercontent.com/Stronger197/764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cityApiService: CityApiService = retrofit.create()
}