package com.example.myapplication.ui.favorites

import android.view.View
import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarLogo
import com.example.myapplication.network.VehicleApi
import com.example.myapplication.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class FavoritesViewModel(private val CarDao: CarDao) : ViewModel() {
    private val _carLogo = MutableLiveData<List<CarLogo>>()

    val carLogos: LiveData<List<CarLogo>>
        get() = _carLogo

    init {
        refreshDataFromNetwork()
    }
    fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            _carLogo.value = VehicleApi.retrofitServiceLogos.getCarLogos()
        } catch (networkError: IOException) {
        }
    }

    val allFavoritesCar: LiveData<List<Car>> = CarDao.getAllFavoritesCar().asLiveData()
    val favoritesCarNumber: LiveData<Int> = CarDao.getFavoritesCarNumber().asLiveData()
    fun updateFavorites(car: Car){
        //val newFavoriteValue = if (isUndoAction == null )  else true
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
            mileage = car.mileage,
            image = car.image,
            favorite = !car.favorite,
            color = car.color
        )
        updateCarDatabase(updatedCar)
    }

    fun updateCarDatabase(car: Car) {
        CoroutineScope(Dispatchers.IO).launch {
            CarDao.update(car)
        }
    }

    fun restoreFavorite(car: Car) {
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
            mileage = car.mileage,
            image = car.image,
            favorite = true,
            color = car.color
        )
        updateCarDatabase(updatedCar)
    }
}

class FavoritesViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}