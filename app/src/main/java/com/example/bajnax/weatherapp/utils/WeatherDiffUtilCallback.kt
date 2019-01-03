package com.example.bajnax.weatherapp.utils

import android.support.v7.util.DiffUtil
import com.example.bajnax.weatherapp.model.WeatherEntity

class WeatherDiffUtilCallback(val newList: MutableList<WeatherEntity>, val oldList: MutableList<WeatherEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].apiResponse == newList[newItemPosition].apiResponse
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].apiResponse.id == newList[newItemPosition].apiResponse.id
                && oldList[oldItemPosition].apiResponse.name == newList[newItemPosition].apiResponse.name
                && oldList[oldItemPosition].date == newList[newItemPosition].date)
    }
}