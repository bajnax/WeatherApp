package com.example.bajnax.weatherapp.di

import android.app.Application
import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.room.Room
import android.content.Context
import com.example.bajnax.weatherapp.db.WeatherDao
import com.example.bajnax.weatherapp.db.WeatherDatabase
import dagger.Module


@Module
class RoomModule(private val context: Context) {

    private lateinit var weatherDatabase: WeatherDatabase

    @Singleton
    @Provides
    internal fun providesWeatherDatabase(): WeatherDatabase {
        weatherDatabase = Room.databaseBuilder(context.applicationContext as Application, WeatherDatabase::class.java, "weather-db").build()
        return weatherDatabase
    }

    @Singleton
    @Provides
    internal fun providesWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

}