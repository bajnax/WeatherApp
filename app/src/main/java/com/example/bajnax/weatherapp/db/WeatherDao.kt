package com.example.bajnax.weatherapp.db

import android.arch.persistence.room.*
import io.reactivex.Flowable


@Dao
interface WeatherDao {

    @Transaction
    @Query("SELECT * FROM city WHERE city_name=:cityName")
    fun getWeatherByCityName(cityName: String): Flowable<CityWithWeather>

    @Transaction
    @Query("SELECT * FROM city")
    fun getAllWeatherRecords(): Flowable<List<CityWithWeather>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(city: City)

    @Insert
    fun insertCityWeather(cityWeather: CityWeather)

    @Transaction
    fun insertOrUpdateWeather(city: City, cityWeather: CityWeather) {
        insertCity(city)
        insertCityWeather(cityWeather)
    }

}
