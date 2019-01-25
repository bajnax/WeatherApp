package com.example.bajnax.weatherapp.ui.activity.blank

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.bajnax.weatherapp.db.CityWithWeather
import com.example.bajnax.weatherapp.presentation.view.blank.ChartView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.bajnax.weatherapp.db.CityWeather
import com.example.bajnax.weatherapp.presentation.presenter.blank.ChartPresenter
import com.example.bajnax.weatherapp.utils.CITY_NAME
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import kotlinx.android.synthetic.main.activity_chart.*
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import com.example.bajnax.weatherapp.R
import android.widget.Toast
import java.text.SimpleDateFormat

class ChartActivity : MvpAppCompatActivity(), ChartView {

    @InjectPresenter
    lateinit var mChartPresenter: ChartPresenter

    @ProvidePresenter
    fun providePresenter(): ChartPresenter = ChartPresenter()

    override fun onWeatherLoaded(cityWithWeather: CityWithWeather) {

        val mDateFormatter = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        var dataPoints = arrayOf<DataPoint>()

        for (weatherDetail: CityWeather in cityWithWeather.weatherList) {
            dataPoints = dataPoints.plus(DataPoint(weatherDetail.timestamp.toDouble(), weatherDetail.celsius.toDouble()))
        }

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(dataPoints)
        series.apply {
            title = cityWithWeather.city.cityName
            isDrawDataPoints = true
            dataPointsRadius = 10f
            thickness = 8
            setOnDataPointTapListener { _, dataPoint ->
                Toast.makeText(
                    this@ChartActivity, "Measurement time: " + simpleDateFormat.format(dataPoint.x) +
                            "\nTemperature value (C): " + dataPoint.y, Toast.LENGTH_SHORT
                ).show()
            }
        }

        graph.titleColor = com.example.bajnax.weatherapp.R.color.colorPrimaryDark
        graph.gridLabelRenderer.verticalAxisTitle = "Temperature (C)"

        graph.viewport.apply {
            setMinY(-50.0)
            setMaxY(50.0)
            isYAxisBoundsManual = true
            isXAxisBoundsManual = true
            setMinX(cityWithWeather.weatherList.first().timestamp.toDouble())
            setMaxX(cityWithWeather.weatherList.last().timestamp.toDouble())
            isScalable = true
            isScrollable = true
        }

        graph.gridLabelRenderer.apply {
            labelsSpace = 20
            padding = 50
            labelFormatter = DateAsXAxisLabelFormatter(this@ChartActivity, mDateFormatter)
            numHorizontalLabels = 3
            setHorizontalLabelsAngle(30)
            setHumanRounding(false)
        }

        graph.addSeries(series)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val cityName: String? = intent.getStringExtra(CITY_NAME)
        cityName?.let {
            mChartPresenter.getWeatherByCityName(cityName)
        }

    }
}