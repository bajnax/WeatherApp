package com.example.bajnax.weatherapp

import android.app.Application
import com.example.bajnax.weatherapp.di.*

class WeatherApp : Application() {
    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerAppComponent.builder().contextModule(ContextModule(this)).roomModule(RoomModule(this)).build()
    }
}