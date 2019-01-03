package com.example.bajnax.weatherapp.model

import java.util.*

data class WeatherEntity(var apiResponse: ApiResponse, val date: Date,
                         val dateString: String, val timeString: String, val fahrenheit: Int, val celsius: Int)