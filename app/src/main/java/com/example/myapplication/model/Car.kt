package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.*


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
    @ColumnInfo val price: Double,
    @ColumnInfo val mileage: Double,
    //@ColumnInfo val color: Double,
    //@ColumnInfo val state: Double,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,
    @ColumnInfo val favorite: Boolean = false
)

fun Car.formatPriceToCurrency(price:Double): String{
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return format.format(price)
}
fun Car.carPowerWithUnitString(kw: Int): String {
    return "$kw kW (${convertKwToCv(kw)}CV)"
}
fun Car.convertKwToCv(kw: Int): Int {
    return (kw * 1.36).toInt()
}
enum class fuelType{
    Petrol,
    Diesel,
    Electric,
    Gas,
    Other
}

