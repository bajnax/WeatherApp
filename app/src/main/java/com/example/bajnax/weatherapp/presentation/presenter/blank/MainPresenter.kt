package com.example.bajnax.weatherapp.presentation.presenter.blank

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.bajnax.weatherapp.WeatherApp
import com.example.bajnax.weatherapp.model.WeatherApi
import com.example.bajnax.weatherapp.presentation.view.blank.MainView
import retrofit2.Retrofit
import javax.inject.Inject
import android.support.annotation.NonNull
import com.example.bajnax.weatherapp.model.ApiResponse
import com.example.bajnax.weatherapp.model.WeatherEntity
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var weatherApi: WeatherApi

    @Inject
    lateinit var retrofit: Retrofit

    var weatherList = mutableListOf<WeatherEntity>()
    lateinit var disposable: DisposableObserver<ApiResponse>

    init {
        WeatherApp.graph.inject(this)
        weatherApi = retrofit.create(WeatherApi::class.java)

    }

    fun getWeatherByGeo(latitude: Double, longitude: Double) {
        disposable = weatherApi.getWeatherByCoordinates(latitude, longitude).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver())
    }

    fun getWeatherByCity(city: String) {
        disposable = weatherApi.getWeatherByName(city).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver())
    }

    private fun getObserver(): DisposableObserver<ApiResponse> {
        return object : DisposableObserver<ApiResponse>() {

            override fun onNext(@NonNull apiResponse: ApiResponse) {
                val date = Date()
                val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val receivedDate = dateFormatter.format(date)
                val receivedTime = timeFormatter.format(date)
                val fahrenheit: Int = (32 + (apiResponse.main.temp - 273.15) * 9/5).roundToInt()
                val celsius: Int = (apiResponse.main.temp - 273.15).roundToInt()
                val weatherEntity = WeatherEntity(apiResponse, date, receivedDate, receivedTime, fahrenheit, celsius)
                weatherList.add(weatherEntity)
                viewState.onWeatherLoaded(weatherList)
                //TODO: cache the date in DB
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) disposable.dispose()
    }

}
