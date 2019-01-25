package com.example.bajnax.weatherapp.db

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity (
    tableName = "city"
)
class City(
    @PrimaryKey
    @ColumnInfo(name = "c_id") var c_id: Int,
    @ColumnInfo(name = "city_name") var cityName: String
)

@Entity (
    tableName = "city_weather",
    foreignKeys = [
        ForeignKey(entity = City::class,
            parentColumns = ["c_id"],
            childColumns = ["city_id"],
            onDelete = CASCADE)]
)
class CityWeather(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "city_id") var city_id: Int,
    @ColumnInfo(name = "fahrenheit") var fahrenheit: Int,
    @ColumnInfo(name = "celsius") var celsius: Int,
    @ColumnInfo(name = "timestamp")
    @TypeConverters(DateConverter::class) var timestamp: Long,
    @ColumnInfo(name = "received_time") var receivedTime: String,
    @ColumnInfo(name = "received_Date") var receivedDate: String
)

class CityWithWeather {
    @Embedded
    var city: City = City(0, "")

    @Relation(parentColumn = "c_id",
        entityColumn = "city_id",
        entity = CityWeather::class)
    var weatherList: List<CityWeather> = listOf()
}
