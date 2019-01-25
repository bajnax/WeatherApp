package com.example.bajnax.weatherapp.model

import android.databinding.BaseObservable


data class WeatherEntity(var cityName: String,
                         var celsius: String,
                         var fahrenheit: String,
                         var dateString: String = "",
                         var timeString: String = "",
                         var isFahrenheit: Boolean = false,
                         var hasGraph: Boolean = false) : BaseObservable()
