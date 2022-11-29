package com.example.myapplication.model

import android.graphics.Color
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
fun Car.carMileageWithUnitString(mileage: Double): String{
    return "$mileage Km"
}
fun Car.convertKwToCv(kw: Int): Int {
    return (kw * 1.36).toInt()
}
enum class fuelType{
    Petrol,
    Diesel,
    Electric,
    Gas,
    BEV,
    HEV,
    MHEV,
    PHEV,
    Other
}
enum class colorCar(
    val nameColor: String,
    val rgbColor: Int
){
    METALLIC_RED("Metallic Red", Color.rgb(192, 14, 26)),
    METALLIC_BLACK("Metallic Black", Color.rgb(13, 17, 22)),
    METALLIC_BRONZE("Metallic Bronze", Color.rgb(145, 101, 50)),
    UTIL_SILVER("Util Silver", Color.rgb(140, 144, 149)),
    MATTE_BLUE("Matte Blue", Color.rgb(37, 58, 167)),
    MATTE_LIME_GREEN("Matte Lime Green", Color.rgb(102, 184, 31)),
    MATTE_ORANGE("Matte Orange", Color.rgb(242, 125, 32)),
    MATTE_PURPLE("Matte Purple", Color.rgb(107, 31, 123)),
    MATTE_WHITE("Matte White", Color.rgb(	252, 249, 241)),
    MATTE_YELLOW("Matte Yellow", Color.rgb(255, 201, 31)),
    MATTE_BROWN("Matte Brown", Color.rgb(101, 63, 35)),
    MATTE_GRAY("Matte Gray", Color.rgb(151, 154, 151))
}

