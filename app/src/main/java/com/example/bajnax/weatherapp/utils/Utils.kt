package com.example.bajnax.weatherapp.utils

import android.databinding.BindingAdapter
import android.view.View

class Utils {
    companion object {
        @JvmStatic
        @BindingAdapter("visibleOrGone")
        fun View.setVisibleOrGone(show: Boolean) {
            visibility = if (show) View.VISIBLE else View.GONE
        }
    }
}