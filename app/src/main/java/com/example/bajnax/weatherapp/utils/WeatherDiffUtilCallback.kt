package com.example.bajnax.weatherapp.utils

import android.support.v7.util.DiffUtil
import com.example.bajnax.weatherapp.model.WeatherEntity

class WeatherDiffUtilCallback(private val newList: MutableList<WeatherEntity>, private val oldList: MutableList<WeatherEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]::class == newList[newItemPosition]::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
