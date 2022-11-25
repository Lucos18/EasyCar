package com.example.myapplication.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.fuelType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    fun getYearToString(year: Int): String = year.toString()

    fun updateFavorites(view: View,car: Car){
            val updatedCar = Car(
                id = car.id,
                brand = car.brand,
                model = car.model,
                yearStartProduction = car.yearStartProduction,
                yearEndProduction = car.yearEndProduction,
                seats = car.seats,
                carPower = car.carPower,
                fuelType = car.fuelType,
                price = car.price,
                image = car.image,
                favorite = !car.favorite
            )
            updateCarDatabase(updatedCar)
        }

        fun updateCarDatabase(car: Car) {
            CoroutineScope(Dispatchers.IO).launch {
                CarDao.update(car)
            }
        }
    }

class HomeViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
