package com.example.bajnax.weatherapp.presentation.view.blank

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.bajnax.weatherapp.model.WeatherEntity


interface MainView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onWeatherLoaded(weatherList: MutableList<WeatherEntity>)

}
