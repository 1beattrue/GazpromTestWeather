package edu.mirea.onebeattrue.gazpromtestweather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.CityApiFactory
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.CityApiService
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.WeatherApiFactory
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.WeatherApiService
import edu.mirea.onebeattrue.gazpromtestweather.data.repository.CityRepositoryImpl
import edu.mirea.onebeattrue.gazpromtestweather.data.repository.WeatherRepositoryImpl
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.CityRepository
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.WeatherRepository

@Module
interface DataModule {

    @[Binds ApplicationScope]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[Binds ApplicationScope]
    fun bindCityRepository(impl: CityRepositoryImpl): CityRepository

    companion object {

        @[Provides ApplicationScope]
        fun provideWeatherApiService(): WeatherApiService = WeatherApiFactory.weatherApiService

        @[Provides ApplicationScope]
        fun provideCityApiService(): CityApiService = CityApiFactory.cityApiService
    }
}
