package com.example.bajnax.weatherapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [City::class, CityWeather::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun weatherDao() : WeatherDao

}
