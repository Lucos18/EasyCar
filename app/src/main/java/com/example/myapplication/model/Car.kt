package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Car")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val brand: String,
    @ColumnInfo val model: String,
    @ColumnInfo(name="year_start_production") val yearStartProduction: Int,
    @ColumnInfo(name="year_end_production") val yearEndProduction: Int?,
    @ColumnInfo(name="car_power") val carPower: Int,
    @ColumnInfo val seats: Int,
    @ColumnInfo(name="fuel_type") val fuelType: String,
    @ColumnInfo val price: Double
)
fun Car.doubleToString(price:Double): String = price.toString()
