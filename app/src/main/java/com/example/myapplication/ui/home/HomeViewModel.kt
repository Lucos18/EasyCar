package com.example.myapplication.ui.home

import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import kotlinx.coroutines.launch

class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()
    //TODO move addCar to section of sell
    fun addCar(
        Brand: String,
        Model: String,
        YearStartProduction: Int,
        YearEndProduction: Int,
        Seats: Int,
        FuelType: String,
        price: Double
    ) {
        val car = Car(
            brand = Brand,
            model = Model,
            yearStartProduction = YearStartProduction,
            yearEndProduction = YearEndProduction,
            seats = Seats,
            fuelType = FuelType,
            price = price
        )

        viewModelScope.launch {
            CarDao.insert(car)
        }
    }

    fun getYearToString(year: Int): String = year.toString()
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
