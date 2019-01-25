package com.example.bajnax.weatherapp.presentation.presenter.blank

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.bajnax.weatherapp.WeatherApp
import com.example.bajnax.weatherapp.db.WeatherDao
import com.example.bajnax.weatherapp.db.WeatherDatabase
import com.example.bajnax.weatherapp.presentation.view.blank.ChartView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChartPresenter : MvpPresenter<ChartView>() {
    @Inject
    lateinit var weatherDatabase: WeatherDatabase
    @Inject
    lateinit var weatherDao: WeatherDao
    init {
        WeatherApp.graph.inject(this)
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCityName(cityName: String) {
        weatherDao.getWeatherByCityName(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ weather ->
                viewState.onWeatherLoaded(weather)
            }, {it.printStackTrace()})
    }

}