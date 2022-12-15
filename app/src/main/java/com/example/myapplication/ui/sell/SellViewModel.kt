package com.example.myapplication.ui.sell

import androidx.lifecycle.*
import com.example.myapplication.data.CarDao
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarLogo
import com.example.myapplication.network.VehicleApi
import kotlinx.coroutines.launch
import java.io.IOException

class SellViewModel(CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

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
}

class SellViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SellViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}