package edu.mirea.onebeattrue.gazpromtestweather.data.network.api

import edu.mirea.onebeattrue.gazpromtestweather.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

object WeatherApiFactory {
    const val BASE_URL = "https://api.openweathermap.org"

    private const val KEY_PARAM = "appid"
    private const val LANG_PARAM = "lang"
    private const val UNITS_PARAM = "units"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val newUrl = originalRequest
                .url()
                .newBuilder()
                .addQueryParameter(KEY_PARAM, BuildConfig.API_KEY)
                .addQueryParameter(LANG_PARAM, Locale.getDefault().language)
                .addQueryParameter(UNITS_PARAM, "metric")
                .build()

            val newRequest = originalRequest
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val weatherApiService: WeatherApiService = retrofit.create()
}