package com.example.myapplication.ui.detailCar

import android.graphics.ColorSpace
import android.media.Image
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.fuelType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Year

class DetailCarViewModel(private val carDao: CarDao) : ViewModel() {

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }

    fun deleteCarById(id: Long) {
        viewModelScope.launch {
            carDao.deleteCarById(id)
        }
    }

    fun updateCar(
        car: Car,
        year: String,
        power: String,
        seats: String,
        fuelType: String,
        price: String,
    ): Boolean {
        val updatedCar = Car(
            id = car.id,
            brand = car.brand,
            model = car.model,
            yearStartProduction = year.toInt(),
            yearEndProduction = car.yearEndProduction,
            seats = seats.toInt(),
            carPower = power.toInt(),
            fuelType = fuelType,
            price = price.toDouble(),
            image = car.image,
        )
        updateCarDatabase(updatedCar)
        return true
    }

    fun updateCarDatabase(car: Car) {
        CoroutineScope(Dispatchers.IO).launch {
            carDao.update(car)
        }
    }

    fun checkInputEditTextNewCar(
        year: Int,
        fuelType: String,
        power: Int,
        seats: Int,
        price: Double
    ): Boolean {
        return year != 0 && fuelType.isNotBlank() && power != 0 && power.toString().length <= 4 && seats != 0 && price != 0.0
    }

}

class DetailViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailCarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailCarViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}