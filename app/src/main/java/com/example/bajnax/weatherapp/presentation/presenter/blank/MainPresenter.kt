package com.example.bajnax.weatherapp.presentation.presenter.blank

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.bajnax.weatherapp.WeatherApp
import com.example.bajnax.weatherapp.model.WeatherApi
import com.example.bajnax.weatherapp.presentation.view.blank.MainView
import retrofit2.Retrofit
import javax.inject.Inject
import android.support.annotation.NonNull
import android.util.Log
import com.example.bajnax.weatherapp.db.*
import com.example.bajnax.weatherapp.model.ApiResponse
import io.reactivex.Completable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import kotlin.math.roundToInt
import io.reactivex.disposables.Disposable
import io.reactivex.CompletableObserver
import java.text.SimpleDateFormat


@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var weatherApi: WeatherApi
    @Inject
    lateinit var weatherDatabase: WeatherDatabase
    @Inject
    lateinit var weatherDao: WeatherDao
    @Inject
    lateinit var retrofit: Retrofit

    private var disposable: DisposableObserver<ApiResponse>? = null
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private var lastCity: String = ""

    init {
        WeatherApp.graph.inject(this)
        weatherApi = retrofit.create(WeatherApi::class.java)

        weatherDao.getAllWeatherRecords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listOfWeathers ->
                if(!listOfWeathers.isEmpty())
                    viewState.onWeatherLoaded(listOfWeathers, lastCity)
                    Log.d("MAINPRESENTER", "Update in DB")
            }, {it.printStackTrace()})
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

    private fun insertOrUpdate(city: City, cityWeather: CityWeather) {

        lastCity = city.cityName

        Completable.fromAction { weatherDao.insertOrUpdateWeather(city, cityWeather) }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {
                    Log.d("MAINPRESENTER", "Added new weather")
                }

                override fun onError(e: Throwable) {
                    Log.e("MAINPRESENTER", "${e.printStackTrace()}")
                }
            })
    }

    private fun getObserver(): DisposableObserver<ApiResponse> {
        return object : DisposableObserver<ApiResponse>() {

            override fun onNext(@NonNull apiResponse: ApiResponse) {
                val date = Date()
                val fahrenheit: Int = (32 + (apiResponse.main.temp - 273.15) * 9/5).roundToInt()
                val celsius: Int = (apiResponse.main.temp - 273.15).roundToInt()
                val receivedTime = timeFormatter.format(date)
                val receivedDate = dateFormatter.format(date)

                val city = City(
                    c_id = apiResponse.id,
                    cityName = apiResponse.name
                )

                val cityWeather = CityWeather(
                    id = 0,
                    city_id = apiResponse.id,
                    fahrenheit = fahrenheit,
                    celsius = celsius,
                    timestamp = DateConverter.toLong(date),
                    receivedTime = receivedTime,
                    receivedDate = receivedDate
                )

                insertOrUpdate(city, cityWeather)
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
        disposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
    }
}
