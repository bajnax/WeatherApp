package com.example.bajnax.weatherapp.model

import com.example.bajnax.weatherapp.utils.API_KEY
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    /**
     * Get the Weather by its name
     */
    @GET("data/2.5/weather")
    fun getWeatherByName(@Query("q") cityName : String,
                         @Query("appid")key: String = API_KEY): Observable<ApiResponse>

    /**
     * Get the Weather by coordinates
     */
    @GET("data/2.5/weather")
    fun getWeatherByCoordinates(@Query("lat") latitude: Double, @Query("lon") longitude: Double,
                                @Query("appid")key: String = API_KEY): Observable<ApiResponse>
}