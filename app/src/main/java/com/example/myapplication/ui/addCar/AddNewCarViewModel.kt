package com.example.myapplication.ui.addCar

import android.graphics.Bitmap
import android.media.Image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {
    fun checkInputEditTextNewCar(
        brand: String,
        year: Int,
        model: String,
        fuelType: String,
        power: Int,
        seats: Int,
        price: Double
    ): Boolean {
        return brand.isNotBlank() && year != 0 && model.isNotBlank() && fuelType.isNotBlank() && power != 0 && seats != 0 && price != 0.0
    }

    fun addCar(
        Brand: String,
        Model: String,
        YearStartProduction: Int,
        YearEndProduction: Int?,
        Seats: Int,
        CarPower: Int,
        FuelType: String,
        Price: Double,
        Image: Bitmap?
    ) {
        val car = Car(
            brand = Brand,
            model = Model,
            yearStartProduction = YearStartProduction,
            yearEndProduction = YearEndProduction,
            seats = Seats,
            carPower = CarPower,
            fuelType = FuelType,
            price = Price,
            image = Image?.toByteArray()
        )

        viewModelScope.launch {
            carDao.insert(car)
        }
    }

    fun convertKwToCv(kw: Int): Int {
        return (kw * 1.36).toInt()
    }
    private fun Bitmap.toByteArray(quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }
}

class AddNewCarViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewCarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewCarViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}