package com.example.bajnax.weatherapp

import android.app.Application
import com.example.bajnax.weatherapp.di.AppComponent
import com.example.bajnax.weatherapp.di.DaggerAppComponent

class WeatherApp : Application() {
    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerAppComponent.create()
    }
}