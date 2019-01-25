package com.example.bajnax.weatherapp.presentation.view.blank

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.bajnax.weatherapp.db.CityWithWeather

interface ChartView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onWeatherLoaded(cityWithWeather: CityWithWeather)
}