package com.example.bajnax.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bajnax.weatherapp.BR
import com.example.bajnax.weatherapp.R
import com.example.bajnax.weatherapp.model.WeatherEntity
import com.example.bajnax.weatherapp.ui.activity.blank.ChartActivity


class WeatherAdapter(private val context: Context, var data: MutableList<WeatherEntity>): RecyclerView.Adapter<WeatherViewHolder>() {

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(data[position], this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.weather_item, parent, false)

        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    /*fun dispatch(newList: MutableList<WeatherEntity>) {
        val userDiffCallback = WeatherDiffUtilCallback(newList, data)
        val diffResult = DiffUtil.calculateDiff(userDiffCallback)
        data.clear()
        data.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }*/

    fun prepareChartData(cityName: String)
    {
        val intent = Intent(context, ChartActivity::class.java)
        intent.putExtra(CITY_NAME, cityName)
        context.startActivity(intent)
    }

}

class WeatherViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: WeatherEntity, adapter: WeatherAdapter) {
        binding.run {
            setVariable(BR.item, data)
            setVariable(BR.adapter, adapter)
            executePendingBindings()
        }
    }
}
