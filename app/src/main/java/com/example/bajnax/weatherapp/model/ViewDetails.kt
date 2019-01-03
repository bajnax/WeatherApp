package com.example.bajnax.weatherapp.model

import android.databinding.BaseObservable

data class ViewDetails(var isFahrenheit: Boolean = true, var color: Int, var cityName: String = "",
                       var temp: Int = 0, var show: Boolean = false) : BaseObservable()