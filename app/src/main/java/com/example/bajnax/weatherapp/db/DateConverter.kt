package com.example.bajnax.weatherapp.db

import android.arch.persistence.room.TypeConverter
import java.util.*


object DateConverter {

    @TypeConverter
    fun toDate(value: Long): Date {
        return  Date(value)
    }

    @TypeConverter
    fun toLong(value: Date): Long {
        return value.time
    }
}
