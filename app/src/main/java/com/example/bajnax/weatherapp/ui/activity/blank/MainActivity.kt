package com.example.bajnax.weatherapp.ui.activity.blank

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.HORIZONTAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.bajnax.weatherapp.R
import com.example.bajnax.weatherapp.presentation.view.blank.MainView
import com.example.bajnax.weatherapp.presentation.presenter.blank.MainPresenter
import com.example.bajnax.weatherapp.databinding.ActivityMainBinding
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.bajnax.weatherapp.BR
import com.example.bajnax.weatherapp.db.CityWithWeather
import com.example.bajnax.weatherapp.model.ViewDetails
import com.example.bajnax.weatherapp.model.WeatherEntity
import com.example.bajnax.weatherapp.utils.WeatherAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import com.tbruyelle.rxpermissions2.RxPermissions


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mMainPresenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = MainPresenter()

    private lateinit var binding: ActivityMainBinding

    private val rxPermissions = RxPermissions(this)
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLastLocation: Location? = null
    private var weatherAdapter: WeatherAdapter? = null
    private lateinit var viewDetails: ViewDetails
    private lateinit var currentWeather: CityWithWeather
    private lateinit var currentWeatherList: MutableList<WeatherEntity>

    // fills the recyclerView with the filtered list
    override fun onWeatherLoaded(weatherList: List<CityWithWeather>, lastCityName: String) {
        currentWeather = if(lastCityName.isEmpty())
            weatherList.last()
        else
            weatherList.last { it.city.cityName == lastCityName }

        updateCurrentWeather()

        currentWeatherList = weatherList.map { it ->
            val hasGraph: Boolean = it.weatherList.size > 1
            WeatherEntity(
                it.city.cityName,
                it.weatherList.last().celsius.toString(),
                it.weatherList.last().fahrenheit.toString(),
                it.weatherList.last().receivedDate,
                it.weatherList.last().receivedTime,
                viewDetails.isFahrenheit,
                hasGraph
            )
        }.toMutableList()

        if(weatherAdapter == null) {
            weatherAdapter = WeatherAdapter(this, currentWeatherList)
            weatherRecyclerView.adapter = weatherAdapter
        } else {
            updateAdapter()
        }
    }

    private fun updateAdapter() {
        for (item: WeatherEntity in currentWeatherList) {
            item.isFahrenheit = viewDetails.isFahrenheit
        }
        // TODO: replace with observable DiffUtil
        weatherAdapter = WeatherAdapter(this, currentWeatherList)
        weatherRecyclerView.adapter = weatherAdapter
    }

    // adjusts the appearance of the main view
    private fun updateCurrentWeather() {
        viewDetails.apply {
            cityName = currentWeather.city.cityName

            color = when {
                currentWeather.weatherList.last().fahrenheit in 50..77 -> ContextCompat.getColor(this@MainActivity, R.color.lightYellow)
                currentWeather.weatherList.last().fahrenheit > 77 -> ContextCompat.getColor(this@MainActivity, R.color.lightOrange)
                else -> ContextCompat.getColor(this@MainActivity, R.color.lightBlue)
            }
            show = true
            temp = if (viewDetails.isFahrenheit) {
                currentWeather.weatherList.last().fahrenheit
            } else {
                currentWeather.weatherList.last().celsius
            }
        }

        binding.setVariable(BR.details, viewDetails)
        binding.notifyChange()
        binding.executePendingBindings()
    }

    companion object {
        const val TAG = "MainActivity"
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherRecyclerView.layoutManager = LinearLayoutManager(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewDetails = ViewDetails(true, Color.BLUE)
        binding.setVariable(BR.details, viewDetails)
        binding.notifyChange()
        binding.executePendingBindings()

        tempTypeSwitch.setOnCheckedChangeListener { _, _ -> run {
                viewDetails.isFahrenheit = !viewDetails.isFahrenheit
                updateAdapter()
                updateCurrentWeather()
            }
        }
    }

    // retrieves the last known location via FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                    mLastLocation?.let{
                        mMainPresenter.getWeatherByGeo(it.latitude, it.longitude)
                    }
                } else {
                    Log.d(TAG, "getLastLocation:exception", task.exception)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let{ mMainPresenter.getWeatherByCity(it) }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    // requests the location permission. Proceeds to call, if granted
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_gps_location -> {
            rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        getLastLocation()
                    } else {
                        finish()
                    }
                }
            true }
        else -> super.onOptionsItemSelected(item)
    }
}
