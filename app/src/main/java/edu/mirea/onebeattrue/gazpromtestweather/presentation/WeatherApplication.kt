package edu.mirea.onebeattrue.gazpromtestweather.presentation

import android.app.Application
import edu.mirea.onebeattrue.gazpromtestweather.di.DaggerApplicationComponent

class WeatherApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}